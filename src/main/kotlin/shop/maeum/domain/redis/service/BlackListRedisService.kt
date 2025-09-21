package shop.maeum.domain.redis.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class BlackListRedisService(
    private val redisTemplate: StringRedisTemplate,
    template: StringRedisTemplate
) {

    fun setBlackList(token: String, type: String, exp: Long) {
        redisTemplate.opsForValue()
            .set(token, type, exp, TimeUnit.MILLISECONDS)
    }

    fun isBlacklisted(token: String): Boolean {
        return redisTemplate.hasKey(token)
    }

}