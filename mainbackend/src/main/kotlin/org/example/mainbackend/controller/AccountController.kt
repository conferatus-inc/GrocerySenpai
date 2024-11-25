package org.example.mainbackend.controller

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.example.mainbackend.configuration.JwtUtils
import org.example.mainbackend.exception.ServerExceptions
import org.example.mainbackend.model.Role
import org.example.mainbackend.model.enums.RoleName
import org.example.mainbackend.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.util.stream.Collectors

@RestController
@RequestMapping(path = ["/api/v1/accounts"])
@Slf4j // TODO: fix, delete or save
class AccountController(
    private val accountService: AccountService,
    private val jwtUtils: JwtUtils,
) {
    @GetMapping("/users")
    fun findAllUsers(): ResponseEntity<*> {
        return ResponseEntity.ok(accountService.findAllUsers())
    }

    @GetMapping("/users/info")
    fun getUserInfo(
        @RequestBody form: UserNameForm,
    ): ResponseEntity<*> {
        return ResponseEntity.ok(accountService.getUser(form.username))
    }

    @GetMapping("/roles")
    fun fundAllRoles(): ResponseEntity<MutableList<Role>> {
        return ResponseEntity.ok(accountService.findAllRoles())
    }

    @PostMapping("/users/addrole")
    fun addRoleToUser(
        @RequestBody form: RoleToUserForm,
    ): ResponseEntity<*> {
        return ResponseEntity.ok(accountService.addRoleToUser(form.username, form.role))
    }

    @DeleteMapping("/users/delete")
    fun deleteUser(
        @RequestBody userName: UserNameForm,
    ): ResponseEntity<*> {
        accountService.deleteUser(userName.username)
        return ResponseEntity.ok("Role $userName was deleted")
    }

    @GetMapping("/token/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        log.info("User trying to logout")
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                val refreshToken = authorizationHeader.substring("Bearer ".length)
                val decodedJWT = jwtUtils.decodeJWT(refreshToken)
                val username = decodedJWT.subject
                val user = accountService.getUser(username)
                accountService.updateAccessToken(username, "")
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                log.warn("User {} refresh own tokens", username)
                ObjectMapper().writeValue(
                    response.outputStream,
                    mapOf(
                        "Information" to "logout",
                        "roles" to user.roles.stream().map(Role::name).collect(Collectors.toList()),
                        "username" to user.username,
                    ),
                )
            } catch (e: IOException) {
                log.error("Error with logout {}", e.message)
                response.setHeader("error", e.message)
                response.status = HttpStatus.UNAUTHORIZED.value()
                val error: MutableMap<String, String?> = HashMap()
                error["error_message"] = e.message
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                try {
                    ObjectMapper().writeValue(
                        response.outputStream,
                        error,
                    )
                } catch (ex: IOException) {
                    throw RuntimeException(ex)
                }
            }
        } else {
            log.info("NOT TOKEN AUTHENTICATION")
            response.status = HttpStatus.BAD_REQUEST.value()
            val error: MutableMap<String, String> = HashMap()
            error["error_message"] = "NOT TOKEN AUTHENTICATION"
            error["status"] = response.status.toString() + ""
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            try {
                ObjectMapper().writeValue(
                    response.outputStream,
                    error,
                )
            } catch (e: IOException) {
                response.status = HttpStatus.BAD_REQUEST.value()
            }
        }
    }

    @PostMapping("/token/refresh")
    fun refreshToken(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        log.info("User trying to refresh token")
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                val refreshToken = authorizationHeader.substring("Bearer ".length)
                var decodedJWT: DecodedJWT? = null
                try {
                    decodedJWT = jwtUtils.decodeJWT(refreshToken)
                } catch (e: TokenExpiredException) {
                    // TODO: добавить мб другие кэтчи, или обобщить по-другому
                    ServerExceptions.REFRESH_TOKEN_EXPIRED.throwException()
                } catch (e: JWTVerificationException) {
                    log.error("Error refresh")
                    ServerExceptions.BAD_REFRESH_TOKEN.throwException()
                }
                val username = decodedJWT!!.subject
                val user = accountService.getUser(username)
                val oldRefreshToken = user.refreshToken
                if (oldRefreshToken != refreshToken) {
                    ServerExceptions.NOT_CURRENT_REFRESH_TOKEN.moreInfo("NOT current refresh token").throwException()
                }
                val tokens = jwtUtils.createTokens(user)

                accountService.updateAccessToken(username, tokens.accessToken)
                accountService.updateRefreshToken(username, tokens.refreshToken)
                response.setHeader("access_token", tokens.accessToken)
                response.setHeader("refresh_token", tokens.refreshToken)
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                log.warn("User {} refresh own tokens", username)
                ObjectMapper().writeValue(
                    response.outputStream,
                    mapOf(
                        "access_token" to tokens.accessToken,
                        "refresh_token" to tokens.refreshToken,
                        "roles" to user.roles.stream().map(Role::name).collect(Collectors.toList()),
                        "username" to user.username,
                    ),
                )
            } catch (e: IOException) {
                log.error("Error logging with {}", e.message)
                response.setHeader("error", e.message)
                response.status = HttpStatus.FORBIDDEN.value()
                val error: MutableMap<String, String?> = HashMap()
                error["error_message"] = e.message
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                try {
                    ObjectMapper().writeValue(
                        response.outputStream,
                        error,
                    )
                } catch (ex: IOException) {
                    throw RuntimeException(ex)
                }
            }
        } else {
            log.info("NOT TOKEN AUTHENTICATION")
            response.status = ServerExceptions.BAD_REFRESH_TOKEN.status()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            ServerExceptions.BAD_REFRESH_TOKEN.moreInfo("REFRESH TOKEN DOESN'T PROVIDED")
        }
    }

    @GetMapping("/login")
    fun login(
        @RequestHeader(value = "Authorization") token: String,
        @RequestHeader(value = "role") role: RoleName,
    ): ResponseEntity<Map<String, Any>> {
        val userLoginDTO = accountService.login(token, role)
        val yResponse = userLoginDTO.responseYandexId
        val user = userLoginDTO.user
        val tokens = jwtUtils.createTokens(user)
        return ResponseEntity.ok(
            mapOf(
                "access_token" to tokens.accessToken,
                "refresh_token" to tokens.refreshToken,
                "roles" to user.roles.stream().map(Role::name).toList(),
                "username" to user.username,
                "display_name" to yResponse.display_name,
            ),
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}

data class RoleNameForm(val role: String)

data class UserNameForm(val username: String)

data class RoleToUserForm(val username: String, val role: RoleName)
