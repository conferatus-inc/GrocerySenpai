package org.example.mainbackend.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.example.mainbackend.configuration.JwtUtils;
import org.example.mainbackend.dto.UserLoginDTO;
import org.example.mainbackend.exception.ServerExceptions;
import org.example.mainbackend.model.Role;
import org.example.mainbackend.model.User;
import org.example.mainbackend.model.enums.RoleName;
import org.example.mainbackend.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequestMapping(path = "/api/accounts")
@AllArgsConstructor
@Slf4j
//TODO: fix, delete or save
public class AccountController {
    private final AccountService accountService;

    private final JwtUtils jwtUtils;

    @GetMapping({"/users"})
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(accountService.getUsers());
    }

    @GetMapping({"/users/info"})
    public ResponseEntity<?> getUserInfo(@RequestBody UserNameForm form) {
        return ResponseEntity.ok(accountService.getUser(form.username()));

    }


    @GetMapping({"/roles"})
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(accountService.getRoles());
    }

//    @GetMapping({"/clear/cache"})
//    public ResponseEntity<?> clearCache() {
//        accountService.clearCache();
//        return ResponseEntity.ok("Cache was cleared");
//    }

    @PostMapping({"/users/addrole"})
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        return ResponseEntity.ok(accountService.addRoleToUser(form.username(), form.role()));
    }

//    @PostMapping({"roles/add"})
//    public ResponseEntity<?> saveRole(@RequestBody Role role) {
//        try {
//            return ResponseEntity.ok(accountService.saveRole(role));
//        } catch (ResponseException e) {
//            return e.response();
//        }
//    }

//    @PostMapping({"/roles/retrieve"})
//    public ResponseEntity<?> deleteRoleFromUser(@RequestBody RoleToUserForm form) {
//        try {
//            return ResponseEntity.ok(accountService.deleteRoleFromUser(form.getUsername(), form.getRole()));
//        } catch (ResponseException e) {
//            return e.response();
//        }
//    }
//
//    @PostMapping({"/root/roles/delete", "/admin/roles/delete"})
//    public ResponseEntity<?> deleteRole(@RequestBody RoleNameForm roleName) {
//        try {
//            accountService.deleteRole(roleName.getRole());
//            return ResponseEntity.ok("Role " + roleName + " was deleted");
//        } catch (ResponseException e) {
//            return e.response();
//        }
//    }

    @DeleteMapping({"/users/delete"})
    public ResponseEntity<?> deleteUser(@RequestBody UserNameForm userName) {
        accountService.deleteUser(userName.username());
        return ResponseEntity.ok("Role " + userName + " was deleted");
    }

//    @GetMapping("/token/info")
//    public void getInfo(HttpServletRequest request, HttpServletResponse response) {
//        log.info("User trying to refresh token");
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                String access_token = authorizationHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256(CustomSecurityConfig.secretWord.getBytes());
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(access_token);
//                String username = decodedJWT.getSubject();
//                AppUser user = accountService.getUser(username);
//                String access_token1 = user.getAccess_token();
//                if (!access_token1.equals(access_token)) {
//                    ResponseException.throwResponse(HttpStatus.UNAUTHORIZED, "It's not current refresh token");
//                }
//
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                log.warn("User {} refresh own tokens", username);
//                new ObjectMapper().writeValue(response.getOutputStream(),
//                        Map.of("username", user.getUsername(),
//                                "roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList())));
//            } catch (ResponseException e) {
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                try {
//                    new ObjectMapper().writeValue(response.getOutputStream(), new ResponseException.Response(e.httpStatus, e.reason));
//                } catch (IOException ex) {
//                    response.setStatus(500);
//                }
//                //return ResponseEntity.badRequest().body("There is no user");
//            } catch (Exception e) {
//                log.error("Error logging with {}", e.getMessage());
//                response.setHeader("error", e.getMessage());
//                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                Map<String, String> error = new HashMap<>();
//                error.put("error_message", e.getMessage());
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                try {
//                    new ObjectMapper().writeValue(response.getOutputStream(),
//                            error);
//                } catch (IOException ex) {
//                    response.setStatus(500);
//                }
//            }
//        } else {
//            log.info("NOT TOKEN AUTHENTICATION");
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            Map<String, String> error = new HashMap<>();
//            error.put("error_message", "NOT TOKEN AUTHENTICATION");
//            error.put("status", response.getStatus() + "");
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            try {
//                new ObjectMapper().writeValue(response.getOutputStream(),
//                        error);
//            } catch (IOException e) {
//                response.setStatus(HttpStatus.BAD_REQUEST.value());
//            }
//
//        }
//    }

    @GetMapping("/token/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("User trying to logout");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = jwtUtils.decodeJWT(refresh_token);
                String username = decodedJWT.getSubject();
                User user = accountService.getUser(username);
                accountService.updateAccessToken(username, "");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                log.warn("User {} refresh own tokens", username);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        Map.of("Information", "logout",
                                "roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                                "username", user.getUsername()));
            } catch (IOException e) {
                log.error("Error with logout {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            error);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            log.info("NOT TOKEN AUTHENTICATION");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "NOT TOKEN AUTHENTICATION");
            error.put("status", response.getStatus() + "");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(),
                        error);
            } catch (IOException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
        }
    }


    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("User trying to refresh token");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = null;
                try {
                    decodedJWT = jwtUtils.decodeJWT(refresh_token);
                }
                //TODO: добавить мб другие кэтчи, или обобщить по-другому
                catch (TokenExpiredException e) {
                    ServerExceptions.REFRESH_TOKEN_EXPIRED.throwException();
                }
                catch (JWTVerificationException e) {
                    log.error("Error refresh");
                    ServerExceptions.BAD_REFRESH_TOKEN.throwException();
                }
                String username = decodedJWT.getSubject();
                User user = accountService.getUser(username);
                String oldRefreshToken = user.getRefreshToken();
                if (!oldRefreshToken.equals(refresh_token)) {
                    ServerExceptions.NOT_CURRENT_REFRESH_TOKEN.moreInfo("NOT current refresh token").throwException();
                }
                JwtUtils.Tokens tokens = jwtUtils.createTokens(user);

                accountService.updateAccessToken(username, tokens.access_token());
                accountService.updateRefreshToken(username, tokens.refresh_token());
                response.setHeader("access_token", tokens.access_token());
                response.setHeader("refresh_token", tokens.refresh_token());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                log.warn("User {} refresh own tokens", username);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        Map.of("access_token", tokens.access_token(),
                                "refresh_token", tokens.refresh_token(),
                                "roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                                "username", user.getUsername()));
            } catch (IOException e) {
                log.error("Error logging with {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            error);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            log.info("NOT TOKEN AUTHENTICATION");
            response.setStatus(ServerExceptions.BAD_REFRESH_TOKEN.status());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ServerExceptions.BAD_REFRESH_TOKEN.moreInfo("REFRESH TOKEN DOESN'T PROVIDED");
        }
    }

//    @PostMapping("/registry")
//    public ResponseEntity<?> registry(@RequestHeader(value = "Authorization") String token,
//                                      @RequestHeader(value = "role") String role) {
//        AppUser res = accountService.createAppUser(token, Set.of(new Role(Roles.USER),
//                new Role("ROLE_" + role.toUpperCase())));
//        return ResponseEntity.ok(res);
//    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader(value = "Authorization", required = false) String token,
                                   @RequestHeader(value = "role") RoleName role) {
        UserLoginDTO userLoginDTO = accountService.login(token, role);
        var yResponse = userLoginDTO.responseYandexId();
        User user = userLoginDTO.appUser();
        JwtUtils.Tokens tokens = jwtUtils.createTokens(user);
        return ResponseEntity.ok(Map.of("access_token", tokens.access_token(),
                "refresh_token", tokens.refresh_token(),
                "roles", user.getRoles().stream().map(Role::getName).toList(),
                "username", user.getUsername(),
                "display_name", yResponse.display_name()
        ));
    }
}

record RoleNameForm(String role) {
}

record UserNameForm(String username) {
}

record RoleToUserForm(String username, RoleName role) {
}