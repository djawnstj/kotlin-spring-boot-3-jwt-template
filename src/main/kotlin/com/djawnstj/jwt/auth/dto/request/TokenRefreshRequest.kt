package com.djawnstj.jwt.auth.dto.request

data class TokenRefreshRequest(
    val refreshToken: String,
)