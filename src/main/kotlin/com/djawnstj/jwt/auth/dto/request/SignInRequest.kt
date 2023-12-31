package com.djawnstj.jwt.auth.dto.request

data class SignInRequest(
    val loginId: String,
    val password: String,
)