package com.djawnstj.jwt.user.api

import com.djawnstj.jwt.auth.web.LoginUser
import com.djawnstj.jwt.user.dto.request.ChangeUserInfoRequest
import com.djawnstj.jwt.user.dto.request.SignUpRequest
import com.djawnstj.jwt.user.dto.response.ChangeUserInfoResponse
import com.djawnstj.jwt.user.dto.response.SignUpResponse
import com.djawnstj.jwt.user.dto.response.UserListResponse
import com.djawnstj.jwt.user.dto.response.UserResponse
import com.djawnstj.jwt.user.entity.User
import com.djawnstj.jwt.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/api/v1/users")
    fun signUp(@RequestBody request: SignUpRequest): SignUpResponse =
        userService.signUp(request)

    @GetMapping("/api/v1/users")
    fun getUsers(): UserListResponse =
        userService.getUserList()

    @GetMapping("/api/v1/users/{loginId}")
    fun getUserByLoginId(@PathVariable("loginId") loginId: String): UserResponse =
        userService.getUser(loginId)

    @PutMapping("/api/v1/users")
    fun changeUserInfo(@LoginUser user: User, @RequestBody request: ChangeUserInfoRequest): ChangeUserInfoResponse =
        userService.changeUserInfo(user, request)

    @DeleteMapping("/api/v1/users")
    fun withdraw(@LoginUser user: User): Boolean =
        userService.withdraw(user)

}