package org.example.mainbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yandex.shbr.ost.backend.controller.dto.UserLoginDTO;
import ru.yandex.shbr.ost.backend.controller.root.dto.RequestUser;
import ru.yandex.shbr.ost.backend.entities.user.AppUser;
import ru.yandex.shbr.ost.backend.entities.user.Role;
import ru.yandex.shbr.ost.backend.exceptions.ResponseException;
import ru.yandex.shbr.ost.backend.exceptions.TypicalServerExceptions;
import ru.yandex.shbr.ost.backend.repository.user.AppUserRepository;
import ru.yandex.shbr.ost.backend.repository.user.RoleRepository;
import ru.yandex.shbr.ost.backend.security.role.Roles;
import ru.yandex.shbr.ost.backend.services.YandexIdService;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.shbr.ost.backend.exceptions.TypicalServerExceptions.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceJava {
    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final WeakHashMap<String, String> aTokenMap = new WeakHashMap<>();
    private final WeakHashMap<String, AppUser> userCache = new WeakHashMap<>();
    private final Set<String> authURLs = new HashSet<>();
    private final YandexIdService yandexIdService;

    public boolean needAuthorisation(String url) {
        return authURLs.contains(url);
    }

    public void addAuthURL(String url) {
        authURLs.add(url);
    }


//    public Role saveRole(Role role) throws ResponseException {
//        Roles.greaterPermission(role.getName());
//        log.info("Save new role with name {}", role.getName());
//        return roleRepository.save(role);
//    }

//    public AppUser createAppUser(String token, Collection<Role> roles) {
//        if (roles == null) {
//            roles = new ArrayList<>();
//        }
//        Long id = yandexIdService.getId(yandexIdService.parseToken(token));
//
//        return createAppUser(id.toString(), id.toString(), roles.stream().map(Role::getName).collect(Collectors.toSet()));
//    }

    public AppUser createAppUser(String userName, String password, Set<String> roles) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        //Roles.greaterPermission(roles);
        checkPassword(password);
        checkUserName(userName);
        if (userRepository.findByUsername(userName).isPresent()) {
            TypicalServerExceptions.USER_ALREADY_EXISTS.throwException();
        }
        log.info("Adding new user {} with {}", userName, roles);
        log.info("password in reg:{}", password);
        List<String> notExists = new ArrayList<>();
        Set<Role> exists = new HashSet<>();
        for (String role : roles) {
            var foundedRole = roleRepository.findByName(role);
            foundedRole.ifPresent(exists::add);
            if (foundedRole.isEmpty()) {
                notExists.add(role);
            }
        }
        if (!notExists.isEmpty()) {
            log.error("for {}: Roles not exists {}", userName, notExists);
            TypicalServerExceptions.ROLE_NOT_EXISTS.moreInfo("Roles not exists: " + notExists).throwException();
        }
        AppUser newUser = new AppUser();
        newUser.setRoles(exists);
        newUser.setUsername(userName);
        String savedPassword = encoder.encode(password);
        newUser.setPassword(savedPassword);
        log.info("saved password:{}", savedPassword);
        log.info("encoded:" + encoder.encode(password));
        userCache.put(userName, newUser);
        log.info("Added new user {} with roles {}", userName, roles);
        return userRepository.save(newUser);
    }


    public AppUser addRoleToUser(String userName, String roleName) throws ResponseException {
        log.info("Start adding role {} to user {}", roleName, userName);

        var user = findUser(userName);
        if (user.isEmpty()) {
            log.warn("User {} not found", userName);
            throw new ResponseException(HttpStatus.NOT_FOUND, "User not found: " + userName);
        }
        var role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            log.warn("Role {} not found", roleName);
            throw new ResponseException(HttpStatus.NOT_FOUND, "Role not found: " + roleName);
        }
        log.info("Added role {} to user {}", roleName, userName);
        user.get().getRoles().add(role.get());
        user.get().setAccess_token("");
        aTokenMap.remove(user.get().getUsername());
        return user.get();
    }

    @Transactional
    public AppUser addRoleToUser(Long id, String addRole, String removeRole) {
        var userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            USER_NOT_FOUND.throwException();
        }
        var user = userO.get();
        if (addRole != null && Roles.isUserRole(addRole)) {
            user.getRoles().add(roleRepository.findByName(addRole).get());
        }
        if (removeRole != null && Roles.isUserRole(addRole)) {
            user.getRoles().remove(roleRepository.findByName(removeRole).get());
        }
        return user;
    }


    public AppUser getUser(String username) {
        log.info("Getting user {}", username);
        Optional<AppUser> user = findUser(username);
        if (user.isPresent()) {
            return user.get();
        }
        USER_NOT_FOUND.moreInfo("User " + username + " not found").throwException();
        throw new RuntimeException("WTF?!");
    }

    public AppUser loginWithPassword(String userName, String password) {
        Optional<AppUser> appUserOptional = findUser(userName);
        if (appUserOptional.isEmpty()) {
            USER_NOT_FOUND.throwException();
        }
        AppUser user = appUserOptional.get();
        if (encoder.matches(password, user.getPassword())) {
            return user;
        }
        BAD_PASSWORD.throwException();
        return null;
    }

    public UserLoginDTO login(String token) {
        YandexIdService.ResponseYandexId response = yandexIdService.getId(yandexIdService.parseToken(token));
        long id = Long.parseLong(response.id());
        Optional<AppUser> userO = userRepository.findByUsername(Long.toString(id));
        if (userO.isEmpty()) {
            USER_NOT_FOUND.throwException();
        }
        return new UserLoginDTO(response, userO.get());
    }

    public UserLoginDTO login(String token, String role) {
        log.info("starting logging with token:{} and role {}", token.substring(0, 20), role);
        YandexIdService.ResponseYandexId response = yandexIdService.getId(yandexIdService.parseToken(token));
        String id = response.id();
        Optional<AppUser> userO = userRepository.findByUsername(id);
        if (userO.isEmpty()) {
            AppUser newUser = new AppUser();
            newUser.setUsername(response.id());
            newUser.setNickname(response.display_name());
            if (response.display_name() == null) {
                newUser.setNickname(response.real_name());
            }
            var roleFound = roleRepository.findByName(role);
            if (roleFound.isEmpty()) {
                ROLE_NOT_EXISTS.throwException();
            }
            var userRole = roleRepository.findByName("ROLE_USER");
            if (userRole.isEmpty()) {
                ROLE_NOT_EXISTS.moreInfo("ROLE_USER not exists").throwException();
            }
            newUser.setRoles(Set.of(userRole.get(), roleFound.get()));
            newUser = userRepository.save(newUser);
            userO = Optional.of(newUser);
        }
        return new UserLoginDTO(response, userO.get());
    }

    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

    public List<Role> getRoles() {
        log.info("Get all roles");
        return roleRepository.findAll();
    }


    //todo: add email checking
    public static void checkUserName(String username) {
        if (username == null || username.isBlank()) {
            log.warn("Null or empty username");
            BAD_LOGIN.moreInfo("There is no login").throwException();
        }
        if (!username.matches("(\\w)+")) {
            log.warn("Bad username {}", username);
            BAD_LOGIN.moreInfo("Bad username. Username must contains" +
                    "only digits letters").throwException();
        }
    }

    public static void checkPassword(String password) {
        if (password == null || password.isBlank()) {
            log.warn("Null password in checking");
            BAD_PASSWORD.moreInfo("There is no password").throwException();
        }
        if (!password.matches("(\\S)+")) {
            BAD_PASSWORD.moreInfo("Password must contains" +
                    "only digits letters and signs").throwException();
        }
    }

//    public static void checkUser(AppUser user) throws ResponseException {
//        if (user.getRoles() == null) {
//            user.setRoles(new HashSet<>());
//        }
//        Roles.greaterPermission(user.getRoles().stream().map(Role::toString).collect(Collectors.toList()));
//        checkUserName(user.getUsername());
//        checkPassword(user.getPassword());
//    }

//    public static void checkRole(Role role) throws ResponseException {
//        if (role == null) {
//            log.warn("Null role in checking");
//            throw new ResponseException(HttpStatus.BAD_REQUEST, "There is no role");
//        }
//        if (!role.getName().matches("(\\S)+")) {
//            log.warn("Bad role name {}", role.getName());
//            throw new ResponseException(HttpStatus.BAD_REQUEST, "Bad roleName. roleName must contains" +
//                    "only digits letters");
//        }
//
//    }

//    public void deleteRole(String roleName) throws ResponseException {
//        Roles.greaterPermission(roleName);
//        if (Roles.isBasicRole(roleName)) {
//            log.warn("Trying to delete basic roles");
//            ResponseException.throwResponse(HttpStatus.BAD_REQUEST, "DONT DELETE BASIC ROLES");
//        }
//        if (!roleExists(roleName)) {
//            log.warn("No role {} for delete", roleName);
//            ResponseException.throwResponse(HttpStatus.BAD_REQUEST, "There is no role in data");
//        }
//        Role role = roleRepository.findByName(roleName).get();
//        for (AppUser user : getUsers()) {
//            var roles = user.getRoles();
//            if (roles.contains(role)) {
//                user.removeRole(role);
//                user.setAccess_token("");
//                aTokenMap.remove(user.getUsername());
//
//            }
//
//        }
//        roleRepository.deleteByName(roleName);
//        log.info("Deleted role {}", roleName);
//    }

    public void deleteUser(String username) throws ResponseException {
        if (username == null) {
            ResponseException.throwResponse(HttpStatus.BAD_REQUEST, "There is no username");
        }
        aTokenMap.remove(username);
        AppUser appUser = getUser(username);
        Roles.greaterPermission(appUser.getRoles());
        appUser.getRoles().clear();
        userCache.remove(username);
        userRepository.deleteByUsername(username);
        log.info("Deleted user {}", username);
    }

    public boolean roleExists(Role role) {
        return roleExists(role.getName());
    }

    public boolean roleExists(String roleName) {
        return roleRepository.findByName(roleName).isPresent();
    }

    public AppUser changePassword(String username, String newPassword) throws ResponseException {
        AppUser user = getUser(username);
        Roles.greaterPermission(user.getRoles());
        checkPassword(newPassword);
        user.setPassword(encoder.encode(newPassword));
        return user;
    }

    @Transactional
    public void updateRefreshToken(String username, String refreshToken) {
        var user = getUser(username);
        user.setRefresh_token(refreshToken);
        log.info("refresh token {}", refreshToken);
        log.info("Refresh token updated {}-{}", username, refreshToken);
    }

    @Transactional
    public void updateAccessToken(String username, String accessToken) {
        var user = getUser(username);

        user.setAccess_token(accessToken);
        log.info("access token {}", accessToken);
        aTokenMap.put(username, accessToken);
        log.info("Access token updated {}-{}", username, accessToken);
//        saveUser(user);

    }

    public String getRefreshToken(String username) throws ResponseException {
        log.warn("get user refresh: {}", getUser(username));
        log.warn("get user {} accessToken", username);
        return getUser(username).getRefresh_token();

    }

    public String getAccessToken(String username) {
        log.info("user accessToken {}", username);
        String token = aTokenMap.get(username);
        if (token != null) {
            return token;
        }

        log.warn("get user {} accessToken", username);
        return getUser(username).getAccess_token();

    }

    private Optional<AppUser> findUser(String userName) {
        AppUser userTmp = userCache.get(userName);
        Optional<AppUser> user;
        if (userTmp != null) {
            user = Optional.of(userTmp);
        } else {
            user = userRepository.findByUsername(userName);
        }
        return user;
    }


    public AppUser addUser(RequestUser requestUser) {
        if (userRepository.existsByUsername(requestUser.username())) {
            USER_ALREADY_EXISTS.throwException();
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(requestUser.username());
        appUser.setNickname(requestUser.nickname());
        appUser.setPassword(encoder.encode(requestUser.username() + "password"));
        appUser.setRoles(requestUser.roles()
                .stream()
                .map(roleRepository::findByName)
                .map((roleO) -> {
                    if (roleO.isEmpty()) {
                        return null;
                    } else {
                        return roleO.get();
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        return userRepository.save(appUser);
    }

    public AppUser addPictureToUser(Long id, String pictureBase64) {
        var userO = userRepository.findAppUserById(id);
        if (userO.isEmpty()) {
            USER_NOT_FOUND.throwException();
        }
        var user = userO.get();
        return userRepository.save(user);

    }
}