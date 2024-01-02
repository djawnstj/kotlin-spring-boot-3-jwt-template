package com.djawnstj.jwt.auth.service

import com.djawnstj.jwt.auth.config.JwtProperties
import com.djawnstj.jwt.auth.entity.AuthenticationCredentials
import com.djawnstj.jwt.auth.entity.Credentials
import com.djawnstj.jwt.auth.entity.Identifier
import com.djawnstj.jwt.user.entity.User
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    private val jwtProperties: JwtProperties,
    private val objectMapper: ObjectMapper
) {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun extractUsername(token: String): String = extractAllClaims(token)?.subject.isValidClaimFromToken()

    fun extractJti(token: String): String = extractAllClaims(token)?.id.isValidClaimFromToken()

    private fun String?.isValidClaimFromToken(): String =
        if (isNullOrBlank()) throw IllegalArgumentException("Invalid Refresh Token.")
        else this

    private fun extractAllClaims(token: String): Claims? =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

    fun generateAccessToken(userDetails: UserDetails, jti: String): String =
        buildToken(userDetails, jti, jwtProperties.accessTokenExpiration)

    fun generateRefreshToken(userDetails: UserDetails, jti: String): String =
        buildToken(userDetails, jti, jwtProperties.refreshTokenExpiration)

    fun generateAuthenticationCredentials(user: User): AuthenticationCredentials {
        val jti = UUID.randomUUID().toString()

        val accessToken = generateAccessToken(user, jti)
        val refreshToken = generateRefreshToken(user, jti)

        val credentials = Credentials(accessToken, refreshToken)
        val identifier = Identifier(jti, user.id)

        return AuthenticationCredentials(credentials, identifier)
    }

    fun buildToken(userDetails: UserDetails, jti: String, expiration: Long, additionalClaims: Map<String, Any> = emptyMap()): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .add(additionalClaims)
            .id(jti)
            .and()
            .signWith(secretKey)
            .compact()

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        (extractUsername(token) == userDetails.username) && isTokenActive(token)

    private fun isTokenActive(token: String): Boolean = extractExpiration(token)?.after(Date()) ?: false

    private fun extractExpiration(token: String): Date? = extractAllClaims(token)?.expiration

    fun checkTokenExpiredByTokenString(token: String): Boolean {
        val parts = token.split(".")

        if (parts.size != 3) throw IllegalArgumentException("Invalid Token.")

        val payload = String(Base64.getDecoder().decode(parts[1]))

        val expiration =
            objectMapper.readValue(payload, object : TypeReference<MutableMap<String, String>>() {})["exp"]?.toLong()
            ?: throw IllegalArgumentException("Invalid Token.")

        val current = System.currentTimeMillis() / 1000

        return expiration < current
    }

}