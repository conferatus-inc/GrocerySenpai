package org.example.mainbackend.controller

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.example.mainbackend.configuration.JwtUtils
import org.example.mainbackend.exception.ServerException
import org.example.mainbackend.exception.ServerExceptions
import org.example.mainbackend.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.collections.ArrayList

@Slf4j
@Component
class CustomAuthorizationFilter(
    private val accountService: AccountService,
    private val jwtUtils: JwtUtils,
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD")
        //        response.addHeader("Access-Control-Allow-Headers", "username, password, role, content-type, Origin, Authorization, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Allow-Headers", "*")
        response.addHeader(
            "Access-Control-Expose-Headers",
            "Access-Control-Allow-Origin, Access-Control-Allow-Credentials",
        )
        response.addHeader("Access-Control-Allow-Credentials", "true")
        response.addIntHeader("Access-Control-Max-Age", 10)

        if (request.method == "OPTIONS") {
            response.status = 200
            return
        }

        if (accountService.notNeedAuthorisation(request.servletPath)) {
            log.info("Without authorization {}", request.servletPath)
            try {
                filterChain.doFilter(request, response)
            } catch (serverException: ServerException) {
                log.error(ObjectMapper().writeValueAsString(serverException.answer))
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.status = serverException.status.value()
                ObjectMapper().writeValue(
                    response.outputStream,
                    serverException.code + " " + serverException.message,
                )
            }
        } else {
            log.info("With authorization {}", request.servletPath)
            val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    val token = authorizationHeader.substring("Bearer ".length)
                    val decodedJWT = jwtUtils.decodeJWT(token)
                    val username = decodedJWT.subject
                    log.info("user trying authorize: {}", username)
                    val user = accountService.getUser(username)
                    val oldToken = user.accessToken
                    if (oldToken == null) {
                        log.warn("There is no access token for {}", username)
                        ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("There is no access token for $username")
                            .throwException()
                    }
                    if (oldToken!!.isEmpty()) {
                        log.warn("There is no access token for {}", username)
                        ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("Refresh your token").throwException()
                    }

                    if (oldToken != token) {
                        log.warn("It's not current access token {}", username)
                        ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("It's not current access token").throwException()
                    }

                    val roles = decodedJWT.getClaim("roles").asArray(String::class.java)
                    val authorities: MutableCollection<SimpleGrantedAuthority> = ArrayList()
                    roles.forEach { role: String? ->
                        authorities.add(
                            SimpleGrantedAuthority(role),
                        )
                    }
                    val authenticationToken =
                        UsernamePasswordAuthenticationToken(user, authorities, authorities)

                    val copy = SecurityContextHolder.getContext()
                    copy.authentication = authenticationToken
                    SecurityContextHolder.setContext(copy)
                    log.info("User {} authorized", username)
                    filterChain.doFilter(request, response)
                } catch (e: ServerException) {
                    log.warn("ServerException exception {}", e.answer)
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.status = e.status.value()
                    ObjectMapper().writeValue(
                        response.outputStream,
                        e.answer,
                    )
                } catch (e: TokenExpiredException) {
                    log.warn(
                        "Token expired {}",
                        request.getHeader(HttpHeaders.AUTHORIZATION).substring("Bearer ".length),
                    )
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.status = ServerExceptions.ACCESS_TOKEN_PROBLEM.status()
                    ObjectMapper().writeValue(
                        response.outputStream,
                        ServerExceptions.ACCESS_TOKEN_PROBLEM.moreInfo("Token expired").answer,
                    )
                } catch (e: JWTVerificationException) {
                    log.error("Error logging in {}", e.message)
                    response.setHeader("error", e.message)
                    response.status = ServerExceptions.ILLEGAL_ACCESS_TOKEN.status()
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    ObjectMapper().writeValue(
                        response.outputStream,
                        ServerExceptions.ILLEGAL_ACCESS_TOKEN.answer,
                    )
                }
            } else {
                log.info("NOT TOKEN AUTHENTICATION")
                response.status = ServerExceptions.NO_ACCESS_TOKEN.status()
                ObjectMapper().writeValue(
                    response.outputStream,
                    ServerExceptions.NO_ACCESS_TOKEN.answer,
                )
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
