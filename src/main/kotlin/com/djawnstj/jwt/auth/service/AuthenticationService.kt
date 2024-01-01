package com.djawnstj.jwt.auth.service

import com.djawnstj.jwt.auth.config.JwtProperties
import com.djawnstj.jwt.auth.dto.request.SignInRequest
import com.djawnstj.jwt.auth.dto.request.TokenRefreshRequest
import com.djawnstj.jwt.auth.dto.response.SignInResponse
import com.djawnstj.jwt.auth.dto.response.TokenRefreshResponse
import com.djawnstj.jwt.auth.entity.TokenCache
import com.djawnstj.jwt.auth.repository.TokenRedisRepository
import com.djawnstj.jwt.user.entity.User
import com.djawnstj.jwt.user.repository.UserQueryRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthenticationService(
    private val tokenRedisRepository: TokenRedisRepository,
    private val userQueryRepository: UserQueryRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val jwtProperties: JwtProperties,
) {

    fun login(request: SignInRequest): SignInResponse {
        val user: User = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.loginId, request.password)
        ).principal as User

        val (accessToken, refreshToken) = jwtService.generateTokenPair(user)

        saveToken(refreshToken, user)

        return SignInResponse(accessToken, refreshToken)
    }


    fun refreshToken(request: TokenRefreshRequest): TokenRefreshResponse {
        val presentedRefreshToken = request.refreshToken

        val loginId: String = jwtService.extractUsername(presentedRefreshToken).isValidToken()

        val user = userQueryRepository.findByLoginId(loginId).isValidUser()

        val foundLoginId = tokenRedisRepository.findByToken(presentedRefreshToken).isValidToken()

        validateRefreshToken(presentedRefreshToken, user, foundLoginId)

        val (accessToken, refreshToken) = jwtService.generateTokenPair(user)

        saveToken(refreshToken, user)

        return TokenRefreshResponse(accessToken, refreshToken)
    }

    private fun saveToken(refreshToken: String, user: User) =
        tokenRedisRepository.save(TokenCache(refreshToken, user.loginId, jwtProperties.refreshTokenExpiration))

    private fun validateRefreshToken(jwt: String, user: User, loginId: String) {
        if (user.loginId != loginId) throw IllegalArgumentException("Invalid Refresh Token.")
        if (!jwtService.isTokenValid(jwt, user)) throw IllegalArgumentException("Invalid Refresh Token.")
    }

    private fun User?.isValidUser(): User = this ?: throw IllegalArgumentException("Use Not Found.")

    private fun String?.isValidToken(): String =
        if (isNullOrBlank()) throw IllegalArgumentException("Invalid Refresh Token.")
        else this

}


