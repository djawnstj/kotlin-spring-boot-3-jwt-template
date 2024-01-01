package com.djawnstj.jwt.auth.api

import com.djawnstj.jwt.auth.dto.request.SignInRequest
import com.djawnstj.jwt.auth.dto.request.TokenRefreshRequest
import com.djawnstj.jwt.auth.dto.response.SignInResponse
import com.djawnstj.jwt.auth.dto.response.TokenRefreshResponse
import com.djawnstj.jwt.auth.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/api/v1/auth/login")
    fun signIn(@RequestBody request: SignInRequest): SignInResponse =
        authenticationService.login(request)

    @PostMapping("/api/v1/auth/refresh")
    fun refreshToken(@RequestBody request: TokenRefreshRequest): TokenRefreshResponse =
        authenticationService.refreshToken(request)

}