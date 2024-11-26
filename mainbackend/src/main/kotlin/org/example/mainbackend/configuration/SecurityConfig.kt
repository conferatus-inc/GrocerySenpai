package org.example.mainbackend.configuration

import org.example.mainbackend.controller.CustomAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customAuthorizationFilter: CustomAuthorizationFilter,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                with(auth) {
                    requestMatchers(LOGIN_URL).permitAll()
                    requestMatchers(REFRESH_URL).permitAll()
                    anyRequest().authenticated()
                }
            }
            .addFilterBefore(customAuthorizationFilter, AnonymousAuthenticationFilter::class.java)
            .build()
    }

    companion object {
        const val LOGIN_URL = "/api/v1/accounts/login"
        const val REFRESH_URL = "/api/v1/accounts/token/refresh"
    }
}
