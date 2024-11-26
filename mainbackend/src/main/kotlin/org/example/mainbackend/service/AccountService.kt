package org.example.mainbackend.service

import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.example.mainbackend.configuration.SecurityConfig.Companion.LOGIN_URL
import org.example.mainbackend.configuration.SecurityConfig.Companion.REFRESH_URL
import org.example.mainbackend.dto.RequestUser
import org.example.mainbackend.dto.UserLoginDTO
import org.example.mainbackend.exception.ServerExceptions
import org.example.mainbackend.model.Role
import org.example.mainbackend.model.User
import org.example.mainbackend.model.enums.RoleName
import org.example.mainbackend.repository.RoleRepository
import org.example.mainbackend.repository.UserRepository
import org.example.mainbackend.role.Roles
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashSet

@Service
@Transactional
@Slf4j
class AccountService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val yandexIdService: YandexIdService,
) {
    private val authURLs: MutableSet<String> = HashSet()
    private val notAuthURLs: MutableSet<String> = hashSetOf(LOGIN_URL, REFRESH_URL)

    fun needAuthorisation(url: String): Boolean {
        return authURLs.contains(url)
    }

    fun notNeedAuthorisation(url: String): Boolean {
        return notAuthURLs.contains(url)
    }

    fun addAuthURL(url: String) {
        authURLs.add(url)
    }

    fun createAppUser(
        userName: String?,
        roles: Set<String?>?,
    ): User {
        var roles = roles
        if (roles == null) {
            roles = HashSet()
        }
        // Roles.greaterPermission(roles);
        checkUserName(userName)
        if (userRepository.findByUsername(userName!!) != null) {
            ServerExceptions.USER_ALREADY_EXISTS.throwException()
        }
        log.info("Adding new user {} with {}", userName, roles)
        val notExists: MutableList<String?> = ArrayList()
        val exists: MutableSet<Role> = HashSet()
        for (role in roles) {
            val foundedRole = roleRepository.findByName(RoleName.valueOf(role!!))
            if (foundedRole != null) {
                exists.add(foundedRole)
            } else {
                notExists.add(role)
            }
        }
        if (notExists.isNotEmpty()) {
            log.error("for {}: Roles not exists {}", userName, notExists)
            ServerExceptions.ROLE_NOT_EXISTS.moreInfo("Roles not exists: $notExists").throwException()
        }
        val newUser = User(username = userName)
        newUser.roles = exists
        log.info("Added new user {} with roles {}", userName, roles)
        return userRepository.save(newUser)
    }

    fun addRoleToUser(
        userName: String,
        roleName: RoleName,
    ): User? {
        log.info("Start adding role {} to user {}", roleName, userName)

        val user = findUser(userName)
        if (user == null) {
            log.warn("User {} not found", userName)
            ServerExceptions.NOT_FOUND.moreInfo("User not found: $userName").throwException()
        }
        val role = roleRepository.findByName(roleName)
        if (role == null) {
            log.warn("Role {} not found", roleName)
            ServerExceptions.NOT_FOUND.moreInfo("Role not found: $roleName").throwException()
        }
        log.info("Added role {} to user {}", roleName, userName)
        user!!.roles.add(role!!)
        user.accessToken = ""
        return user
    }

    @Transactional
    fun addRoleToUser(
        id: Long,
        addRole: RoleName?,
        removeRole: RoleName?,
    ): User {
        val userO = userRepository.findById(id)
        if (userO.isEmpty) {
            ServerExceptions.USER_NOT_FOUND.throwException()
        }
        val user = userO.get()
        if (addRole != null && Roles.isUserRole(addRole)) {
            user.roles.add(roleRepository.findByName(addRole)!!)
        }
        if (removeRole != null && Roles.isUserRole(addRole)) {
            user.roles.remove(roleRepository.findByName(removeRole))
        }
        return user
    }

    fun getUser(username: String): User {
        log.info("Getting user {}", username)
        val user = findUser(username)
        if (user == null) {
            ServerExceptions.USER_NOT_FOUND.moreInfo("User $username not found").throwException()
        }
        return user!!
    }

    fun login(token: String): UserLoginDTO {
        val response = yandexIdService.getId(yandexIdService.parseToken(token))!!
        val id = response.id.toLong()
        val userO = userRepository.findByUsername(id.toString())
        if (userO == null) {
            ServerExceptions.USER_NOT_FOUND.throwException()
        }
        return UserLoginDTO(response, userO!!)
    }

    fun login(
        token: String,
        role: RoleName,
    ): UserLoginDTO {
        log.info("starting logging with token:{} and role {}", token.substring(0, 20), role)
        val response = yandexIdService.getId(yandexIdService.parseToken(token))
        val id = response.id
        var userO = userRepository.findByUsername(id)
        if (userO == null) {
            var newUser = User(username = response.id)
            val roleFound = roleRepository.findByName(role)
            if (roleFound == null) {
                ServerExceptions.ROLE_NOT_EXISTS.throwException()
            }
            val userRole = roleRepository.findByName(RoleName.ROLE_USER)
            if (userRole == null) {
                ServerExceptions.ROLE_NOT_EXISTS.moreInfo("ROLE_USER not exists").throwException()
            }
            newUser.roles = mutableSetOf(userRole!!, roleFound!!)
            newUser = userRepository.save(newUser)
            userO = newUser
        }
        return UserLoginDTO(response, userO)
    }

    fun findAllUsers() = userRepository.findAll()

    fun findAllRoles() = roleRepository.findAll()

    fun deleteUser(username: String?) {
        if (username == null) {
            ServerExceptions.BAD_REQUEST.moreInfo("There is no username").throwException()
        }
        val appUser = getUser(username!!)
        Roles.greaterPermission(appUser.roles)
        appUser.roles.clear()
        userRepository.deleteByUsername(username)
        log.info("Deleted user {}", username)
    }

    fun roleExists(role: Role): Boolean {
        return roleRepository.findByName(role.name) != null
    }

    @Transactional
    fun updateRefreshToken(
        username: String,
        refreshToken: String,
    ) {
        val user = getUser(username)
        user.refreshToken = refreshToken
    }

    @Transactional
    fun updateAccessToken(
        username: String,
        accessToken: String,
    ) {
        val user = getUser(username)

        user.accessToken = accessToken

        //        saveUser(user);
    }

    fun getRefreshToken(username: String): String? {
        return getUser(username).refreshToken
    }

    fun getAccessToken(username: String): String? {
        return getUser(username).accessToken
    }

    private fun findUser(userName: String): User? {
        return userRepository.findByUsername(userName)
    }

    fun addUser(requestUser: RequestUser): User {
        if (userRepository.existsByUsername(requestUser.username)) {
            ServerExceptions.USER_ALREADY_EXISTS.throwException()
        }
        val appUser = User(username = requestUser.username)
        appUser.roles =
            requestUser.roles
                .mapNotNull { roleO: RoleName ->
                    roleRepository.findByName(roleO)
                }
                .toMutableSet()
        return userRepository.save(appUser)
    }

    companion object {
        // todo: add email checking
        fun checkUserName(username: String?) {
            if (username.isNullOrBlank()) {
                log.warn("Null or empty username")
                ServerExceptions.BAD_LOGIN.moreInfo("There is no login").throwException()
            }
            if (!username!!.matches("(\\w)+".toRegex())) {
                log.warn("Bad username {}", username)
                ServerExceptions.BAD_LOGIN.moreInfo(
                    "Bad username. Username must contains" +
                        "only digits letters",
                ).throwException()
            }
        }

        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
