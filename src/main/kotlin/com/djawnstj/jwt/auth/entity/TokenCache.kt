package com.djawnstj.jwt.auth.entity

data class TokenCache(
    val key: String,
    val value: Credentials,
    val ttl: Long,
)

data class Credentials(
    val accessToken: String,
    val refreshToken: String,
)