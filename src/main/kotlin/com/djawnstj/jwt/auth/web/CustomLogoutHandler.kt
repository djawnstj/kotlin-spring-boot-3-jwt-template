package com.djawnstj.jwt.auth.web

import com.djawnstj.jwt.auth.repository.TokenRedisRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class CustomLogoutHandler(
    private val tokenRedisRepository: TokenRedisRepository
): LogoutHandler {

    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        val authHeader = request?.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return

        val jwt = authHeader.substring(7)
        tokenRedisRepository.findByToken(jwt)?.let {
            tokenRedisRepository.deleteByToken(jwt)
            SecurityContextHolder.clearContext()
        } ?: run {
            throw IllegalArgumentException("Token Not Found")
        }
    }

}