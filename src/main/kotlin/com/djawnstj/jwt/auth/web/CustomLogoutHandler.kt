package com.djawnstj.jwt.auth.web

import com.djawnstj.jwt.auth.repository.TokenRedisRepository
import com.djawnstj.jwt.auth.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class CustomLogoutHandler(
    private val jwtService: JwtService,
    private val tokenRedisRepository: TokenRedisRepository
): LogoutHandler {

    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        val authHeader = request?.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return

        val jwt = authHeader.substring(7)
        val username = jwtService.extractUsername(jwt)

        tokenRedisRepository.findByLoginId(username)?.let {
            tokenRedisRepository.deleteByLoginId(username)
            SecurityContextHolder.clearContext()
        } ?: throw IllegalArgumentException("Token Not Found")
    }

}