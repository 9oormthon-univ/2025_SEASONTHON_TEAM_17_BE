package shop.maeum.domain.jwt.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import shop.maeum.domain.jwt.component.JwtComponent
import shop.maeum.domain.jwt.utils.JwtAuthenticationTokenExtractor

@Configuration
class JwtConfig {

    @Bean
    fun accessJwtDetails(): JwtDetails = AccessJwtDetails()

    @Bean
    fun refreshJwtDetails(): JwtDetails = RefreshJwtDetails()

    @Bean
    fun jwtComponent(
        @Qualifier("accessJwtDetails") access: JwtDetails,
        @Qualifier("refreshJwtDetails") refresh: JwtDetails
    ): JwtComponent = JwtComponent(access, refresh)

    @Bean
    fun jwtAuthenticationTokenExtractor(jwtComponent: JwtComponent): JwtAuthenticationTokenExtractor =
        JwtAuthenticationTokenExtractor(jwtComponent)

}