package com.djawnstj.jwt.auth.entity

data class TokenCache(
    val token: String,
    val loginId: String,
    val ttl: Long,
)