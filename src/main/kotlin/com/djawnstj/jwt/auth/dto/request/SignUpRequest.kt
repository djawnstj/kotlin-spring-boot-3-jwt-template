package com.djawnstj.jwt.auth.dto.request

import com.djawnstj.jwt.user.entity.Role

data class SignUpRequest(
    val loginId: String,
    val password: String,
    val name: String,
    val role: Role
)