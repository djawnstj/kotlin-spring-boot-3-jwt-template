package com.djawnstj.jwt.auth.config

import com.djawnstj.jwt.auth.web.JwtAuthenticationFilter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties::class)
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val logoutHandler: LogoutHandler
) {

    private companion object {
        private val WHITE_LIST_URLS = arrayOf("/api/v1/auth/**", "/h2-console/**", "/error")
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.run {
            csrf { it.disable() }
            headers { headers ->
                headers.frameOptions { it.disable() }
            }
            authorizeHttpRequests {
                it
                    .requestMatchers(*WHITE_LIST_URLS).permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                    .requestMatchers("/api/users**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .fullyAuthenticated()
            }
            sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            authenticationProvider(authenticationProvider)
            addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            logout {
                it.logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler { req, res, auth -> SecurityContextHolder.clearContext() }
            }
            build()
        }

}