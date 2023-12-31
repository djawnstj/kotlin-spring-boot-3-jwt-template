package com.djawnstj.jwt.auth.dto.response

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
)
