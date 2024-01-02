package com.djawnstj.jwt.auth.service

import com.djawnstj.jwt.auth.config.JwtProperties
import com.djawnstj.jwt.auth.dto.request.SignInRequest
import com.djawnstj.jwt.auth.dto.request.TokenRefreshRequest
import com.djawnstj.jwt.auth.dto.response.SignInResponse
import com.djawnstj.jwt.auth.dto.response.TokenRefreshResponse
import com.djawnstj.jwt.auth.entity.AuthenticationCredentials
import com.djawnstj.jwt.auth.entity.TokenCache
import com.djawnstj.jwt.auth.repository.TokenRedisRepository
import com.djawnstj.jwt.user.entity.User
import com.djawnstj.jwt.user.repository.UserQueryRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
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

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(user)

        saveTokenCache(authenticationCredentials.identifier.public, authenticationCredentials)

        return authenticationCredentials.credentials.run { SignInResponse(accessToken, refreshToken) }
    }

    fun refreshToken(request: TokenRefreshRequest): TokenRefreshResponse {
        val presentedRefreshToken = request.refreshToken

        val loginId: String = jwtService.extractUsername(presentedRefreshToken)
        val jti = jwtService.extractJti(presentedRefreshToken)

        val user = userQueryRepository.findByLoginId(loginId).isValidUser()

        val foundTokens = tokenRedisRepository.findById(jti).isValidToken()

        validateRefreshToken(presentedRefreshToken, foundTokens.credentials.refreshToken, user)

        validateActiveAccessToken(foundTokens.credentials.accessToken, user)

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(user)

        saveTokenCache(authenticationCredentials.identifier.public, authenticationCredentials)
        tokenRedisRepository.deleteById(jti)

        return authenticationCredentials.credentials.run { TokenRefreshResponse(accessToken, refreshToken) }
    }

    private fun validateActiveAccessToken(accessToken: String, user: User) {
        if (!jwtService.checkTokenExpiredByTokenString(accessToken)) throw IllegalArgumentException("Refresh request denied. Active accessToken detected.")
        tokenRedisRepository.deleteById(user.loginId)
    }

    private fun saveTokenCache(uuid: String, authenticationCredentials: AuthenticationCredentials) {
        val tokenCache = TokenCache(uuid, authenticationCredentials, jwtProperties.refreshTokenExpiration)

        tokenRedisRepository.save(tokenCache)
    }

    private fun validateRefreshToken(jwt: String, refreshToken: String, user: UserDetails) {
        if (jwt != refreshToken) throw IllegalArgumentException("Invalid Refresh Token.")
        if (!jwtService.isTokenValid(jwt, user)) throw IllegalArgumentException("Invalid Refresh Token.")
    }

    private fun User?.isValidUser(): User = this ?: throw IllegalArgumentException("Use Not Found.")

    private fun AuthenticationCredentials?.isValidToken(): AuthenticationCredentials =
        this?.let { this } ?: throw IllegalArgumentException("Credentials Not Found.")

}



