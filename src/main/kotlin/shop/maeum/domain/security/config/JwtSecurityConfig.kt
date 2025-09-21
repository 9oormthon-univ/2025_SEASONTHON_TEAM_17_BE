package shop.maeum.domain.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import shop.maeum.domain.jwt.utils.JwtAuthenticationTokenExtractor
import shop.maeum.domain.redis.service.BlackListRedisService
import shop.maeum.domain.security.authentication.JwtAuthenticationProvider
import shop.maeum.domain.security.filter.JwtAuthenticationTokenFilter
import shop.maeum.domain.security.handler.JwtAuthenticationErrorHandler

@Configuration
class JwtSecurityConfig(
    @Value("\${spring.security.cors.allowed-paths}")
    private val whiteList : Array<String>,
    private val redisService: BlackListRedisService
) {
    @Bean
    fun jwtAuthenticationTokenFilter(jwtAuthenticationTokenExtractor: JwtAuthenticationTokenExtractor):
            JwtAuthenticationTokenFilter = JwtAuthenticationTokenFilter(jwtAuthenticationTokenExtractor,
        whiteList, redisService)

    @Bean
    fun jwtAuthenticationProvider(jwtUserDetailService: UserDetailsService):
            JwtAuthenticationProvider = JwtAuthenticationProvider(jwtUserDetailService)

    @Bean
    fun jwtAuthenticationErrorHandler(): JwtAuthenticationErrorHandler = JwtAuthenticationErrorHandler()
}