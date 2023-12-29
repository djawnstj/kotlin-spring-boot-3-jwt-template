package com.djawnstj.jwt.auth.entity

import java.time.LocalDateTime
import java.util.UUID

data class Token(
    val id: String = UUID.randomUUID().toString(),
    var token: String,
    var revoked: Boolean,
    var expired: Boolean,
    var createdAt: LocalDateTime,
    var modifiedAt: LocalDateTime
) {

    fun isValid() = (!this.revoked && !this.expired)

    fun revoke() {
        this.revoked = true
        this.expired = true
    }

    fun isSameRefreshToken(refreshToken: String) = this.token == refreshToken

    fun refresh(refreshToken: String) {
        this.token = refreshToken
        this.revoked = false
        this.expired = false
    }

}
