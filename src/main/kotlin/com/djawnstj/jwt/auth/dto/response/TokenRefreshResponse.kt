package com.djawnstj.jwt.auth.dto.response

data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
