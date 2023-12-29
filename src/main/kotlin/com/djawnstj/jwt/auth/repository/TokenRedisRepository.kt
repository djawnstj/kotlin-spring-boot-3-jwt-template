package com.djawnstj.jwt.auth.repository

import com.djawnstj.jwt.auth.entity.Token
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository

@Repository
class TokenRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val valueOperations: ValueOperations<String, String>
        get() = this.redisTemplate.opsForValue()

    fun findByToken(jwt: String): Token? =
        redisTemplate.keys("*")
            .map { objectMapper.readValue(it, Token::class.java) }
            .find { it.token == jwt }

}