package org.example.mainbackend.role;

import lombok.extern.slf4j.Slf4j;
import org.example.mainbackend.exception.ServerExceptions;
import org.example.mainbackend.model.enums.RoleName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.constant.Constable;
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


    public static Set<RoleName> basicRoles() {
        return Set.of(RoleName.ROLE_USER, RoleName.ROLE_ADMIN);
    }

    public static boolean hasRole(RoleName role) {
        var auths = getAuthorities();
        if (auths == null) {
            return false;
        }
        return auths.contains(role.name());
    }

    public static void mustHaveRole(RoleName role) {
        if (!hasRole(role)) {
            log.warn("Permission denied, doesnt have: {}", role);
            ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs " + role + " properties").throwException();
        }
    }


    public static void mustBeRoot() {
        mustHaveRole(RoleName.ROLE_ROOT);
    }


    public static void mustBeUser() {
        mustHaveRole(RoleName.ROLE_USER);
    }

    public static void mustBeAdmin() {
        mustHaveRole(RoleName.ROLE_ADMIN);
    }

    public static Set<String> getAuthorities() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public static void greaterPermission(Collection<RoleName> roles) {
        Set<RoleName> rols = new HashSet<>(roles);
        if (rols.contains(RoleName.ROLE_ROOT)) {
            log.warn("Permission denied, doesnt have access");
            ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs GOD properties").throwException();
        } else if (rols.contains(RoleName.ROLE_ADMIN)) {
            mustBeRoot();
        } else if (roles.isEmpty()) {
            mustBeUser();
        } else {
            mustBeAdmin();
        }
    }

    public static void greaterPermission(RoleName role) {
        if (role.equals(RoleName.ROLE_ROOT)) {
            ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs God access");
        } else if (role.equals(RoleName.ROLE_ADMIN)) {
            mustBeRoot();
        } else {
            mustBeAdmin();
        }
    }
}
