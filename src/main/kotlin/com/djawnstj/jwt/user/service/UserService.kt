package com.djawnstj.jwt.user.service

import com.djawnstj.jwt.user.dto.request.ChangeUserInfoRequest
import com.djawnstj.jwt.user.dto.request.SignUpRequest
import com.djawnstj.jwt.user.dto.response.ChangeUserInfoResponse
import com.djawnstj.jwt.user.dto.response.SignUpResponse
import com.djawnstj.jwt.user.dto.response.UserListResponse
import com.djawnstj.jwt.user.dto.response.UserResponse
import com.djawnstj.jwt.user.entity.User
import com.djawnstj.jwt.user.repository.UserQueryRepository
import com.djawnstj.jwt.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val userQueryRepository: UserQueryRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun signUp(request: SignUpRequest): SignUpResponse =
        request.let {
            val user = User(it.loginId, passwordEncoder.encode(it.password), it.name, it.role)
            userRepository.save(user)
            SignUpResponse(user.loginId)
        }

    fun getUserList(): UserListResponse =
        UserListResponse(userQueryRepository.findAllNotWithdrawn())

    fun getUser(loginId: String): UserResponse =
        userQueryRepository.findByLoginId(loginId)?.let {
            UserResponse(it.loginId, it.name)
        } ?: throw IllegalArgumentException("User Not Found.")

    @Transactional
    fun changeUserInfo(user: User, request: ChangeUserInfoRequest): ChangeUserInfoResponse {
        user.name = request.name
        userRepository.save(user)

        return ChangeUserInfoResponse(user.loginId, user.name)
    }

    @Transactional
    fun withdraw(user: User): Boolean {
        user.deletedAt = LocalDateTime.now()
        userRepository.save(user)

        return true
    }

}