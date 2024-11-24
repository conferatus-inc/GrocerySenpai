package org.example.mainbackend.role

import lombok.extern.slf4j.Slf4j
import org.example.mainbackend.exception.ServerExceptions
import org.example.mainbackend.model.Role
import org.example.mainbackend.model.enums.RoleName
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

@Slf4j
class Roles {
    companion object {
        private fun basicRoles(): Set<RoleName> {
            return mutableSetOf(RoleName.ROLE_USER, RoleName.ROLE_ADMIN)
        }

        private fun hasRole(role: RoleName): Boolean {
            val auths = getAuthorities() ?: return false
            return auths.contains(role.name)
        }

        private fun mustHaveRole(role: RoleName) {
            if (!hasRole(role)) {
                log.warn("Permission denied, doesnt have: {}", role)
                ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs $role properties").throwException()
            }
        }

        fun isUserRole(role: RoleName?): Boolean {
            return basicRoles().contains(role)
        }

        private fun mustBeRoot() {
            mustHaveRole(RoleName.ROLE_ROOT)
        }

        private fun mustBeUser() {
            mustHaveRole(RoleName.ROLE_USER)
        }

        private fun mustBeAdmin() {
            mustHaveRole(RoleName.ROLE_ADMIN)
        }

        fun greaterPermission(roles: Set<Role>) {
            val roleNames = roles.map(Role::name).toSet()
            if (roleNames.contains(RoleName.ROLE_ROOT)) {
                log.warn("Permission denied, doesnt have access")
                ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs GOD properties").throwException()
            } else if (roleNames.contains(RoleName.ROLE_ADMIN)) {
                mustBeRoot()
            } else if (roles.isEmpty()) {
                mustBeUser()
            } else {
                mustBeAdmin()
            }
        }

        fun greaterPermission(role: RoleName) {
            when (role) {
                RoleName.ROLE_ROOT -> {
                    ServerExceptions.FORBIDDEN.moreInfo("Permission denied, needs God access")
                }
                RoleName.ROLE_ADMIN -> {
                    mustBeRoot()
                }
                else -> {
                    mustBeAdmin()
                }
            }
        }

        private val log = LoggerFactory.getLogger(this::class.java)

        private fun getAuthorities(): Set<String>? {
            if (SecurityContextHolder.getContext().authentication == null) {
                return null
            }
            return SecurityContextHolder.getContext().authentication.authorities
                .map { obj: GrantedAuthority -> obj.authority }.toSet()
        }
    }
}
