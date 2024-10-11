package ru.yandex.shbr.ost.backend.security.appUser

import jakarta.transaction.Transactional
import org.example.mainbackend.exception.ServerExceptions
import org.example.mainbackend.model.Role
import org.example.mainbackend.model.User
import org.example.mainbackend.repository.RoleRepository
import org.example.mainbackend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashSet

@Service
@Transactional
class AccountService (private val userRepository: UserRepository,
                      private val roleRepository: RoleRepository,
                      private val encoder: PasswordEncoder){

    private val authURLs: MutableSet<String> = HashSet()
//    private val yandexIdService: YandexIdService? = null
    fun needAuthorisation(url: String): Boolean {
        return authURLs.contains(url)
    }

    fun addAuthURL(url: String) {
        authURLs.add(url)
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
    fun createAppUser(userName: String, password: String, roles2: Set<String>?): User {
        var roles = roles2
        if (roles == null) {
            roles = HashSet()
        }
        //Roles.greaterPermission(roles);
        checkPassword(password)
        checkUserName(userName)
        userRepository.findByName(userName)?.let {
            ServerExceptions.USER_ALREADY_EXISTS.throwException()
        }

        val notExists: MutableList<String> = ArrayList()
        val exists: Set<Role> = HashSet<Role>()
        for (role in roles) {
            val foundedRole: Unit = roleRepository.findByName(role)
            foundedRole.ifPresent(exists::add)
            if (foundedRole.isEmpty()) {
                notExists.add(role)
            }
        }
        if (!notExists.isEmpty()) {
            log.error("for {}: Roles not exists {}", userName, notExists)
            TypicalServerExceptions.ROLE_NOT_EXISTS.moreInfo("Roles not exists: $notExists").throwException()
        }
        val newUser = AppUser()
        newUser.setRoles(exists)
        newUser.setUsername(userName)
        val savedPassword = encoder!!.encode(password)
        newUser.setPassword(savedPassword)
        log.info("saved password:{}", savedPassword)
        log.info("encoded:" + encoder.encode(password))
        userCache.put(userName, newUser)
        log.info("Added new user {} with roles {}", userName, roles)
        return userRepository.save(newUser)
    }

    @Throws(ResponseException::class)
    fun addRoleToUser(userName: String, roleName: String): AppUser {
        log.info("Start adding role {} to user {}", roleName, userName)
        val user: Optional<AppUser> = findUser(userName)
        if (user.isEmpty()) {
            log.warn("User {} not found", userName)
            throw ResponseException(HttpStatus.NOT_FOUND, "User not found: $userName")
        }
        val role: Unit = roleRepository.findByName(roleName)
        if (role.isEmpty()) {
            log.warn("Role {} not found", roleName)
            throw ResponseException(HttpStatus.NOT_FOUND, "Role not found: $roleName")
        }
        log.info("Added role {} to user {}", roleName, userName)
        user.get().getRoles().add(role.get())
        user.get().setAccess_token("")
        aTokenMap.remove(user.get().getUsername())
        return user.get()
    }

    @Transactional
    fun addRoleToUser(id: Long, addRole: String?, removeRole: String?): AppUser {
        val userO = userRepository!!.findById(id)
        if (userO.isEmpty) {
            USER_NOT_FOUND.throwException()
        }
        val user = userO.get()
        if (addRole != null && Roles.isUserRole(addRole)) {
            user.roles.add(roleRepository.findByName(addRole).get())
        }
        if (removeRole != null && Roles.isUserRole(addRole)) {
            user.roles.remove(roleRepository.findByName(removeRole).get())
        }
        return user
    }

    fun getUser(username: String?): AppUser {
        log.info("Getting user {}", username)
        val user: Optional<AppUser> = findUser(username)
        if (user.isPresent()) {
            return user.get()
        }
        USER_NOT_FOUND.moreInfo("User $username not found").throwException()
        throw RuntimeException("WTF?!")
    }

    fun loginWithPassword(userName: String?, password: String?): AppUser? {
        val appUserOptional: Optional<AppUser> = findUser(userName)
        if (appUserOptional.isEmpty()) {
            USER_NOT_FOUND.throwException()
        }
        val user: AppUser = appUserOptional.get()
        if (encoder!!.matches(password, user.getPassword())) {
            return user
        }
        BAD_PASSWORD.throwException()
        return null
    }

    fun login(token: String?): UserLoginDTO {
        val response: YandexIdService.ResponseYandexId = yandexIdService.getId(yandexIdService.parseToken(token))
        val id: Long = response.id().toLong()
        val userO: Optional<AppUser> = userRepository.findByUsername(java.lang.Long.toString(id))
        if (userO.isEmpty()) {
            USER_NOT_FOUND.throwException()
        }
        return UserLoginDTO(response, userO.get())
    }

    fun login(token: String, role: String?): UserLoginDTO {
        log.info("starting logging with token:{} and role {}", token.substring(0, 20), role)
        val response: YandexIdService.ResponseYandexId = yandexIdService.getId(yandexIdService.parseToken(token))
        val id: String = response.id()
        var userO: Optional<AppUser?> = userRepository.findByUsername(id)
        if (userO.isEmpty()) {
            var newUser = AppUser()
            newUser.setUsername(response.id())
            newUser.setNickname(response.display_name())
            if (response.display_name() == null) {
                newUser.setNickname(response.real_name())
            }
            val roleFound: Unit = roleRepository.findByName(role)
            if (roleFound.isEmpty()) {
                ROLE_NOT_EXISTS.throwException()
            }
            val userRole: Unit = roleRepository.findByName("ROLE_USER")
            if (userRole.isEmpty()) {
                ROLE_NOT_EXISTS.moreInfo("ROLE_USER not exists").throwException()
            }
            newUser.setRoles(java.util.Set.of(userRole.get(), roleFound.get()))
            newUser = userRepository!!.save(newUser)
            userO = Optional.of<AppUser?>(newUser)
        }
        return UserLoginDTO(response, userO.get())
    }

    val users: List<Any>
        get() = userRepository!!.findAll()
    val roles: List<Any>
        get() {
            log.info("Get all roles")
            return roleRepository!!.findAll()
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
    @Throws(ResponseException::class)
    fun deleteUser(username: String?) {
        if (username == null) {
            ResponseException.throwResponse(HttpStatus.BAD_REQUEST, "There is no username")
        }
        aTokenMap.remove(username)
        val appUser: AppUser = getUser(username)
        Roles.greaterPermission(appUser.getRoles())
        appUser.getRoles().clear()
        userCache.remove(username)
        userRepository.deleteByUsername(username)
        log.info("Deleted user {}", username)
    }

    fun roleExists(role: Role): Boolean {
        return roleExists(role.getName())
    }

    fun roleExists(roleName: String?): Boolean {
        return roleRepository.findByName(roleName).isPresent()
    }

    @Throws(ResponseException::class)
    fun changePassword(username: String?, newPassword: String?): AppUser {
        val user: AppUser = getUser(username)
        Roles.greaterPermission(user.getRoles())
        checkPassword(newPassword)
        user.setPassword(encoder!!.encode(newPassword))
        return user
    }

    @Transactional
    fun updateRefreshToken(username: String?, refreshToken: String?) {
        val user: AppUser = getUser(username)
        user.setRefresh_token(refreshToken)
        log.info("refresh token {}", refreshToken)
        log.info("Refresh token updated {}-{}", username, refreshToken)
    }

    @Transactional
    fun updateAccessToken(username: String?, accessToken: String?) {
        val user: AppUser = getUser(username)
        user.setAccess_token(accessToken)
        log.info("access token {}", accessToken)
        aTokenMap.put(username, accessToken)
        log.info("Access token updated {}-{}", username, accessToken)
        //        saveUser(user);
    }

    @Throws(ResponseException::class)
    fun getRefreshToken(username: String?): String {
        log.warn("get user refresh: {}", getUser(username))
        log.warn("get user {} accessToken", username)
        return getUser(username).getRefresh_token()
    }

    fun getAccessToken(username: String?): String {
        log.info("user accessToken {}", username)
        val token: String = aTokenMap.get(username)
        if (token != null) {
            return token
        }
        log.warn("get user {} accessToken", username)
        return getUser(username).getAccess_token()
    }

    private fun findUser(userName: String?): Optional<AppUser> {
        val userTmp: AppUser = userCache.get(userName)
        val user: Optional<AppUser>
        if (userTmp != null) {
            user = Optional.of<AppUser>(userTmp!!)
        } else {
            user = userRepository.findByUsername(userName)
        }
        return user
    }

    fun addUser(requestUser: RequestUser): AppUser {
        if (userRepository.existsByUsername(requestUser.username())) {
            USER_ALREADY_EXISTS.throwException()
        }
        val appUser = User(username = "")
        appUser.setUsername()
        appUser.setNickname(requestUser.nickname())
        appUser.setPassword(encoder!!.encode(requestUser.username() + "password"))
        appUser.setRoles(requestUser.roles()
            .stream()
            .map(roleRepository::findByName)
            .map{
                if (it) {
                    return@map null
                } else {
                    return@map it.get()
                }
            }
            .filter { obj: Any? -> Objects.nonNull(obj) }
            .collect(Collectors.toSet()))
        return userRepository!!.save(appUser)
    }

    fun addPictureToUser(id: Long?, pictureBase64: String?): AppUser {
        val userO: Unit = userRepository.findAppUserById(id)
        if (userO.isEmpty()) {
            USER_NOT_FOUND.throwException()
        }
        val user: Unit = userO.get()
        return userRepository!!.save(user)
    }

    companion object {
        //todo: add email checking
        fun checkUserName(username: String?) {
            if (username == null || username.isBlank()) {
                log.warn("Null or empty username")
                BAD_LOGIN.moreInfo("There is no login").throwException()
            }
            if (!username!!.matches("(\\w)+".toRegex())) {
                log.warn("Bad username {}", username)
                BAD_LOGIN.moreInfo(
                    "Bad username. Username must contains" +
                            "only digits letters"
                ).throwException()
            }
        }

        fun checkPassword(password: String?) {
            if (password == null || password.isBlank()) {
                log.warn("Null password in checking")
                BAD_PASSWORD.moreInfo("There is no password").throwException()
            }
            if (!password!!.matches("(\\S)+".toRegex())) {
                BAD_PASSWORD.moreInfo(
                    "Password must contains" +
                            "only digits letters and signs"
                ).throwException()
            }
        }
    }
}