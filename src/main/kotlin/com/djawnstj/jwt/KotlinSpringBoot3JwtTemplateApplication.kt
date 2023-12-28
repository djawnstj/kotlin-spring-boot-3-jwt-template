package com.djawnstj.jwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class KotlinSpringBoot3JwtTemplateApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringBoot3JwtTemplateApplication>(*args)
}
