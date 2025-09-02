package shop.maeum.domain.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetailsService
import shop.maeum.domain.jwt.utils.JwtAuthenticationTokenExtractor
import shop.maeum.domain.security.authentication.JwtAuthenticationProvider
import shop.maeum.domain.security.filter.JwtAuthenticationTokenFilter

@Configuration
class JwtSecurityConfig {
    @Bean
    fun jwtAuthenticationTokenFilter(jwtAuthenticationTokenExtractor: JwtAuthenticationTokenExtractor):
            JwtAuthenticationTokenFilter = JwtAuthenticationTokenFilter(jwtAuthenticationTokenExtractor)

    @Bean
    fun jwtAuthenticationProvider(jwtUserDetailService: UserDetailsService):
            JwtAuthenticationProvider = JwtAuthenticationProvider(jwtUserDetailService)
}