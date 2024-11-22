package org.example.mainbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mainbackend.dto.RequestUser;
import org.example.mainbackend.dto.UserLoginDTO;
import org.example.mainbackend.exception.ServerExceptions;
import org.example.mainbackend.model.Role;
import org.example.mainbackend.model.User;
import org.example.mainbackend.model.enums.RoleName;
import org.example.mainbackend.repository.RoleRepository;
import org.example.mainbackend.repository.UserRepository;
import org.example.mainbackend.role.Roles;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final YandexIdService yandexIdService;
    private final Set<String> authURLs = new HashSet<>();

    public boolean needAuthorisation(String url) {
        return authURLs.contains(url);
    }

    public void addAuthURL(String url) {
        authURLs.add(url);
    }


    public User createAppUser(String userName, Set<String> roles) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        //Roles.greaterPermission(roles);
        checkUserName(userName);
        if (userRepository.findByUsername(userName) != null) {
            ServerExceptions.USER_ALREADY_EXISTS.throwException();
        }
        log.info("Adding new user {} with {}", userName, roles);
        List<String> notExists = new ArrayList<>();
        Set<Role> exists = new HashSet<>();
        for (String role : roles) {
            var foundedRole = roleRepository.findByName(RoleName.valueOf(role));
            if (foundedRole != null) {
                exists.add(foundedRole);
            }
            else {
                notExists.add(role);
            }
        }
        if (!notExists.isEmpty()) {
            log.error("for {}: Roles not exists {}", userName, notExists);
            ServerExceptions.ROLE_NOT_EXISTS.moreInfo("Roles not exists: " + notExists).throwException();
        }
        User newUser = new User();
        newUser.setRoles(exists);
        newUser.setUsername(userName);
        log.info("Added new user {} with roles {}", userName, roles);
        return userRepository.save(newUser);
    }


    public User addRoleToUser(String userName, RoleName roleName) {
        log.info("Start adding role {} to user {}", roleName, userName);

        var user = findUser(userName);
        if (user == null) {
            log.warn("User {} not found", userName);
            ServerExceptions.NOT_FOUND.moreInfo("User not found: " + userName).throwException();
        }
        var role = roleRepository.findByName(roleName);
        if (role == null ) {
            log.warn("Role {} not found", roleName);
            ServerExceptions.NOT_FOUND.moreInfo("Role not found: " + roleName).throwException();
        }
        log.info("Added role {} to user {}", roleName, userName);
        user.getRoles().add(role);
        user.setAccessToken("");
        return user;
    }

    @Transactional
    public User addRoleToUser(Long id, RoleName addRole, RoleName removeRole) {
        var userO = userRepository.findById(id);
        if (userO.isEmpty()) {
            ServerExceptions.USER_NOT_FOUND.throwException();
        }
        var user = userO.get();
        if (addRole != null && Roles.isUserRole(addRole)) {
            user.getRoles().add(roleRepository.findByName(addRole));
        }
        if (removeRole != null && Roles.isUserRole(addRole)) {
            user.getRoles().remove(roleRepository.findByName(removeRole));
        }
        return user;
    }


    public User getUser(String username) {
        log.info("Getting user {}", username);
        var user = findUser(username);
        if (user == null) {
            ServerExceptions.USER_NOT_FOUND.moreInfo("User " + username + " not found").throwException();
        }
        return user;
    }

    public UserLoginDTO login(String token) {
        YandexIdService.ResponseYandexId response = yandexIdService.getId(yandexIdService.parseToken(token));
        long id = Long.parseLong(response.id());
        var userO = userRepository.findByUsername(Long.toString(id));
        if (userO == null) {
            ServerExceptions.USER_NOT_FOUND.throwException();
        }
        return new UserLoginDTO(response, userO);
    }

    public UserLoginDTO login(String token, RoleName role) {
        log.info("starting logging with token:{} and role {}", token.substring(0, 20), role);
        YandexIdService.ResponseYandexId response = yandexIdService.getId(yandexIdService.parseToken(token));
        String id = response.id();
        var userO = userRepository.findByUsername(id);
        if (userO == null) {
            User newUser = new User();
            newUser.setUsername(response.id());
            var roleFound = roleRepository.findByName(role);
            if (roleFound == null ) {
                ServerExceptions.ROLE_NOT_EXISTS.throwException();
            }
            var userRole = roleRepository.findByName(RoleName.ROLE_USER);
            if (userRole == null) {
                ServerExceptions.ROLE_NOT_EXISTS.moreInfo("ROLE_USER not exists").throwException();
            }
            newUser.setRoles(Set.of(userRole, roleFound));
            newUser = userRepository.save(newUser);
            userO = newUser;
        }
        return new UserLoginDTO(response, userO);
    }

    public List<User> getUsers() {
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
            ServerExceptions.BAD_LOGIN.moreInfo("There is no login").throwException();
        }
        if (!username.matches("(\\w)+")) {
            log.warn("Bad username {}", username);
            ServerExceptions.BAD_LOGIN.moreInfo("Bad username. Username must contains" +
                    "only digits letters").throwException();
        }
    }

    public void deleteUser(String username) {
        if (username == null) {
            ServerExceptions.BAD_REQUEST.moreInfo("There is no username").throwException();
        }
        User appUser = getUser(username);
        Roles.greaterPermission(appUser.getRoles());
        appUser.getRoles().clear();
        userRepository.deleteByUsername(username);
        log.info("Deleted user {}", username);
    }

    public boolean roleExists(Role role) {
        return roleRepository.findByName(role.getName()) != null;
    }

    @Transactional
    public void updateRefreshToken(String username, String refreshToken) {
        var user = getUser(username);
        user.setRefreshToken(refreshToken);
    }

    @Transactional
    public void updateAccessToken(String username, String accessToken) {
        var user = getUser(username);

        user.setAccessToken(accessToken);
//        saveUser(user);

    }

    public String getRefreshToken(String username) {
        return getUser(username).getRefreshToken();

    }

    public String getAccessToken(String username) {
        return getUser(username).getAccessToken();

    }

    private User findUser(String userName) {
        return userRepository.findByUsername(userName);
    }


    public User addUser(RequestUser requestUser) {
        if (userRepository.existsByUsername(requestUser.username())) {
            ServerExceptions.USER_ALREADY_EXISTS.throwException();
        }
        User appUser = new User();
        appUser.setUsername(requestUser.username());
        appUser.setRoles(requestUser.roles()
                .stream()
                .map((roleO) -> roleRepository.findByName(RoleName.valueOf(roleO)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        return userRepository.save(appUser);
    }
}