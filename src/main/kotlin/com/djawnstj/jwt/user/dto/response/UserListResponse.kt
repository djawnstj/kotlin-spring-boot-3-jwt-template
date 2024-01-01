package com.djawnstj.jwt.user.dto.response

import com.djawnstj.jwt.user.entity.User

data class UserListResponse(
    val users: List<User>
)
