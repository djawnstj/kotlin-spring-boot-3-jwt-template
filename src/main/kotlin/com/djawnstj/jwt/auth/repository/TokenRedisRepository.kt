package com.djawnstj.jwt.auth.repository

import com.djawnstj.jwt.auth.entity.TokenCache
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class TokenRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    private val valueOperations: ValueOperations<String, String>
        get() = this.redisTemplate.opsForValue()

    fun save(tokenCache: TokenCache) {
        valueOperations[tokenCache.token] = tokenCache.loginId

        valueOperations.getAndExpire(tokenCache.token, tokenCache.ttl, TimeUnit.MILLISECONDS)
    }

    fun findByToken(jwt: String): String? = valueOperations.get(jwt)

    fun deleteByToken(jwt: String) = redisTemplate.delete(jwt)


}