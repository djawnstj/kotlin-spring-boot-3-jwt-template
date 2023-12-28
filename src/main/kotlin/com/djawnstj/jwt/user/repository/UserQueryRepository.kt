package com.djawnstj.jwt.user.repository

import com.djawnstj.jwt.user.entity.User
import org.springframework.stereotype.Repository

@Repository
class UserQueryRepository(
    private val userRepository: UserRepository
) {

    fun findByLoginId(loginId: String): User? = userRepository.findAll {
        select(
            entity(User::class)
        ).from(
            entity(User::class)
        ).where(
            path(User::deletedAt).isNull()
        )
    }.firstOrNull()

}