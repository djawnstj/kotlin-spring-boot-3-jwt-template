package com.djawnstj.jwt.config

import com.djawnstj.jwt.user.repository.UserQueryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Configuration
class ApplicationConfig(
    private val userRepository: UserQueryRepository
) {

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService {
        userRepository.findByLoginId(it) ?: throw UsernameNotFoundException("User Not Found")
    }

}