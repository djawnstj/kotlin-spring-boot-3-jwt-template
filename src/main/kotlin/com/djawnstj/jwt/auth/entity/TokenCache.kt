package com.djawnstj.jwt.auth.entity

data class TokenCache(
    val key: String,
    val value: AuthenticationCredentials,
    val ttl: Long,
)

data class AuthenticationCredentials(
    val credentials: Credentials,
    val identifier: Identifier,
)

data class Credentials(
    val accessToken: String,
    val refreshToken: String,
)

data class Identifier(
    val public: String,
    val private: Long,
)