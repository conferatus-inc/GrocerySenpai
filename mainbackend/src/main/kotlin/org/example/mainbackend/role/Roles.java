package org.example.mainbackend.role;

import lombok.extern.slf4j.Slf4j;
import org.example.mainbackend.exception.ServerExceptions;
import org.example.mainbackend.model.enums.RoleName;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class Roles {
    public final static String ROOT = "ROLE_ROOT";
    public final static String _ROOT = "ROOT";
    public final static String ADMIN = "ROLE_ADMIN";
    public final static String _ADMIN = "ADMIN";
    public final static String USER = "ROLE_USER";
    public final static String _USER = "USER";


    public static Set<String> basicRoles() {
        return Set.of(ROOT, USER, ADMIN);
    }

    public static Set<String> privacyRoles() {
        return Set.of(ROOT, USER, ADMIN);
    }

    public static boolean isBasicRole(String role) {
        return basicRoles().contains(role);
    }

    public static boolean isUserRole(String role) {
        return basicRoles().contains(role);
    }

    public static boolean hasRole(String role) {
        var auths = getAuthorities();
        if (auths == null) {
            return false;
        }
        return auths.contains(role);
    }

    public static void mustHaveRole(String role) {
        if (!hasRole(role)) {
            log.warn("Permission denied, doesnt have: {}", role);
            ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs " + role + " properties").throwException();
        }
    }


    public static void mustBeRoot() {
        mustHaveRole(Roles.ROOT);
    }

    public static void mustBeAdmin() {
        mustHaveRole(Roles.ADMIN);
    }

    public static void mustBeUser() {
        mustHaveRole(Roles.USER);
    }

    public static Set<String> getAuthorities() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public static void greaterPermission(Collection<String> roles) {
        Set<String> rols = new HashSet<>(roles);
        if (rols.contains(Roles.ROOT)) {
            log.warn("Permission denied, doesnt have access");
            ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs GOD properties").throwException();
        } else if (rols.contains(Roles.ADMIN)) {
            mustBeRoot();
        } else if (roles.isEmpty()) {
            mustBeUser();
        } else {
            mustBeAdmin();
        }
    }

    public static void greaterPermission(Set<RoleName> roles)  {
        greaterPermission(roles.stream().map(Enum::name).collect(Collectors.toList()));
    }

    public static void greaterPermission(String role) {
        if (role.equals(Roles.ROOT)) {
            ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs God access");
        } else if (role.equals(Roles.ADMIN)) {
            mustBeRoot();
        } else {
            mustBeAdmin();
        }
    }
}
