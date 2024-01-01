package com.djawnstj.jwt.auth.web

import org.springframework.security.core.annotation.AuthenticationPrincipal
import java.lang.annotation.ElementType

@Target(AnnotationTarget.VALUE_PARAMETER)
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
@AuthenticationPrincipal
annotation class LoginUser
