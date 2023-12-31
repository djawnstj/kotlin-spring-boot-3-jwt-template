package com.djawnstj.jwt.auth.service

import com.djawnstj.jwt.auth.dto.request.SignInRequest
import com.djawnstj.jwt.auth.dto.request.SignUpRequest
import com.djawnstj.jwt.auth.dto.response.SignInResponse
import com.djawnstj.jwt.auth.dto.response.SignUpResponse
import com.djawnstj.jwt.auth.repository.TokenRedisRepository
import com.djawnstj.jwt.user.entity.User
import com.djawnstj.jwt.user.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthenticationService(
    private val tokenRedisRepository: TokenRedisRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun signUp(request: SignUpRequest): SignUpResponse =
        request.let {
            val user = User(it.loginId, passwordEncoder.encode(it.password), it.name, it.role)
            userRepository.save(user)
            SignUpResponse(user.loginId)
        }

    fun signIn(request: SignInRequest): SignInResponse {
        val user: User = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.loginId, request.password)
        ).principal as User

        val (accessToken, refreshToken) = jwtService.generateTokenPair(user)

        return SignInResponse(accessToken, refreshToken)
    }



}