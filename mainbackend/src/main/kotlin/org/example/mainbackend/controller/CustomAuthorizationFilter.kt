package org.example.mainbackend.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mainbackend.configuration.JwtUtils;
import org.example.mainbackend.exception.ServerException;
import org.example.mainbackend.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.example.mainbackend.exception.ServerExceptions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final AccountService accountService;
    private final JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
//        response.addHeader("Access-Control-Allow-Headers", "username, password, role, content-type, Origin, Authorization, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            return;
        }   

        if (
                !accountService.needAuthorisation(request.getServletPath())
        ) {
            log.info("Without authorization {}", request.getServletPath());
            try {
                filterChain.doFilter(request, response);
            } catch (ServerException serverException) {
                log.error(new ObjectMapper().writeValueAsString(serverException.getAnswer()));
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(serverException.getStatus().value());
                new ObjectMapper().writeValue(response.getOutputStream(),
                        serverException.getCode() + " " + serverException.getMessage());
            }

        } else {
            log.info("With authorization {}", request.getServletPath());
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    DecodedJWT decodedJWT = jwtUtils.decodeJWT(token);
                    String username = decodedJWT.getSubject();
                    log.info("user trying authorize: {}", username);
                    String oldToken = accountService.getAccessToken(username);
                    if (oldToken == null) {
                        log.warn("There is no access token for {}", username);
                        ACCESS_TOKEN_PROBLEM.moreInfo("There is no access token for " + username).throwException();
                    }
                    if (oldToken.equals("")) {
                        log.warn("There is no access token for {}", username);
                        ACCESS_TOKEN_PROBLEM.moreInfo("Refresh your token").throwException();
                    }

                    if (!oldToken.equals(token)) {
                        log.warn("It's not current access token {}", username);
                        ACCESS_TOKEN_PROBLEM.moreInfo("It's not current access token").throwException();
                    }

                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role ->
                            authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, authorities, authorities);

                    var copy = SecurityContextHolder.getContext();
                    copy.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(copy);
                    log.info("User {} authorized", username);
                    filterChain.doFilter(request, response);
                } catch (ServerException e) {
                    log.warn("ServerException exception {}", e.getAnswer());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(e.getStatus().value());
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            e.getAnswer());
                } catch (TokenExpiredException e) {
                    log.warn("Token expired {}", request.getHeader(AUTHORIZATION).substring("Bearer ".length()));
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(ACCESS_TOKEN_PROBLEM.status());
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            ACCESS_TOKEN_PROBLEM.moreInfo("Token expired").getAnswer());
                } catch (JWTVerificationException e) {
                    log.error("Error logging in {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(ILLEGAL_ACCESS_TOKEN.status());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            ILLEGAL_ACCESS_TOKEN.getAnswer());
                }
            } else {
                log.info("NOT TOKEN AUTHENTICATION");
                response.setStatus(NO_ACCESS_TOKEN.status());
                new ObjectMapper().writeValue(response.getOutputStream(),
                        NO_ACCESS_TOKEN.getAnswer());
            }
        }
    }
}
