package org.example.mainbackend.configuration;

import lombok.RequiredArgsConstructor;
import org.example.mainbackend.controller.CustomAuthorizationFilter;
import org.example.mainbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration //implements WebSecurityConfigurerAdapter
{
    private final AccountAuthenticationProvider authenticationProvider;
    private final AccountService accountService;
    private final JwtUtils jwtUtils;


    @Value("${security.type}")
    private String security;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (security.equals("token")) {
//            CustomAuthFilter filter = new CustomAuthFilter(authenticationProvider, accountService);
//            filter.setFilterProcessesUrl("/api/accounts/login");

            http.csrf().disable();
            http.cors().disable();
            http.authorizeHttpRequests()
                    .antMatchers("/accounts/token/refresh", "/accounts/token/info").permitAll()
//                    .antMatchers("/api/accounts/ping").hasRole(Roles._USER);
                    .antMatchers("/api/accounts/ping").permitAll()
                    .antMatchers("/api/accounts/login").permitAll()
            http
//                .antMatcher("/log")
//                .authenticationProvider(authenticationProvider)
//                .httpBasic(withDefaults())
                    .sessionManagement()
                    .sessionCreationPolicy(STATELESS);
//            http.addFilter(filter);
            http.addFilterBefore(new CustomAuthorizationFilter(accountService, jwtUtils), UsernamePasswordAuthenticationFilter.class);
        } else if (security.equals("no")) {
            http.csrf().disable().sessionManagement()
                    .sessionCreationPolicy(STATELESS);

            http.authorizeRequests().antMatchers("/**").permitAll();
        } else {
            http.csrf().disable();
            http.authorizeRequests().antMatchers("/**").hasRole("USER").and().httpBasic();
            http.sessionManagement()
                    .sessionCreationPolicy(STATELESS);
        }


        return http.build();
    }


}