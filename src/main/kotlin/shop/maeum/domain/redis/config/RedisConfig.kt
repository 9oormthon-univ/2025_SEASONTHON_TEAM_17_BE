package shop.maeum.domain.redis.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,
    @Value("\${spring.data.redis.port}")
    private val port: Int,
) {

    @Bean
    fun redisConnectionFactory() : LettuceConnectionFactory =
        LettuceConnectionFactory(RedisStandaloneConfiguration(host, port))

    @Bean
    fun redisTemplate(redisConnectionFactory: LettuceConnectionFactory): StringRedisTemplate
    = StringRedisTemplate(redisConnectionFactory).apply{
        keySerializer = StringRedisSerializer()
        valueSerializer = StringRedisSerializer()
    }
}