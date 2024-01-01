package com.djawnstj.jwt.auth.repository

import com.djawnstj.jwt.auth.entity.Credentials
import com.djawnstj.jwt.auth.entity.TokenCache
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class TokenRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    private val valueOperations: ValueOperations<String, String>
        get() = this.redisTemplate.opsForValue()

    fun save(tokenCache: TokenCache) {
        valueOperations[tokenCache.key] = objectMapper.writeValueAsString(tokenCache.value)

        valueOperations.getAndExpire(tokenCache.key, tokenCache.ttl, TimeUnit.MILLISECONDS)
    }

    fun findByLoginId(loginId: String): Credentials? =
        valueOperations[loginId]?.let { objectMapper.readValue(it, Credentials::class.java) } ?: run { null }

    fun deleteByLoginId(loginId: String) = redisTemplate.delete(loginId)

    fun deleteAll() = redisTemplate.delete(redisTemplate.keys("*"))

}