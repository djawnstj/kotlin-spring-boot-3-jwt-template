package com.djawnstj.jwt.user.repository

import com.djawnstj.jwt.user.entity.User
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long>, KotlinJdslJpqlExecutor {
}