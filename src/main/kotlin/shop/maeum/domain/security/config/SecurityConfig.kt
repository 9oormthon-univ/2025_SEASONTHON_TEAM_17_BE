package shop.maeum.domain.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import shop.maeum.domain.security.filter.JwtAuthenticationTokenFilter
import shop.maeum.domain.security.handler.GlobalAuthEntryPoint
import shop.maeum.domain.security.handler.JwtAuthenticationErrorHandler
import shop.maeum.domain.security.handler.JwtLogoutHandler
import shop.maeum.domain.security.handler.JwtLogoutSuccessHandler

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationTokenFilter: JwtAuthenticationTokenFilter,
    private val corsConfig: CorsConfig,
    @Value("\${spring.security.cors.allowed-paths}")
    private val whiteList : Array<String>,
    private val jwtAuthEntryPoint: GlobalAuthEntryPoint,
    private val jwtAuthenticationErrorHandler: JwtAuthenticationErrorHandler,
    private val logoutHandler: JwtLogoutHandler,
    private val jwtLogoutSuccessHandler: JwtLogoutSuccessHandler,
    @Value("\${spring.security.logout.url}")
    private val logoutUrl: String
) {
    @Bean
    fun filterChain(http: HttpSecurity
    ): SecurityFilterChain {

        http
            .cors {
                it.configurationSource(corsConfigurationSource())
            }.csrf {
                it.disable()
            }.sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }.authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(
                        *whiteList
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore( jwtAuthenticationErrorHandler, JwtAuthenticationTokenFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthEntryPoint)
            }.logout { logout ->
                logout.logoutUrl(logoutUrl)
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
            }
            .httpBasic {
                it.disable()
            }.anonymous {
                it.disable()
            }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowedMethods = corsConfig.allowedMethods
        config.allowedHeaders = corsConfig.allowedHeaders
        config.allowedOrigins = corsConfig.allowedOrigins
        config.allowCredentials = true
        source.registerCorsConfiguration("/**", config)
        return source
    }

}