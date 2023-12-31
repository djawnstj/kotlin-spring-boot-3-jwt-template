package com.djawnstj.jwt.auth.service

import com.djawnstj.jwt.auth.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function

@Service
class JwtService(
    private val jwtProperties: JwtProperties
) {

    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun extractUsername(token: String): String? = extractAllClaims(token)?.subject

    private fun extractAllClaims(token: String): Claims? =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

    fun generateAccessToken(userDetails: UserDetails): String =
        buildToken(userDetails, jwtProperties.accessTokenExpiration)

    fun generateRefreshToken(userDetails: UserDetails): String =
        buildToken(userDetails, jwtProperties.refreshTokenExpiration)

    fun generateTokenPair(userDetails: UserDetails): Pair<String, String> =
        generateAccessToken(userDetails) to generateRefreshToken(userDetails)

    fun buildToken(userDetails: UserDetails, expiration: Long, additionalClaims: Map<String, Any> = emptyMap()): String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean =
        (extractUsername(token) == extractUsername(userDetails.username)) && !isTokenExpired(token)

    private fun isTokenExpired(token: String): Boolean = extractExpiration(token)?.before(Date()) ?: true

    private fun extractExpiration(token: String): Date? = extractAllClaims(token)?.expiration
}