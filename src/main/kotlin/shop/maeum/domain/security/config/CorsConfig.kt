package shop.maeum.domain.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "cors")
class CorsConfig(
    var allowedHeaders: List<String> = listOf("*"),
    var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS"),
) {
    @Value("\${cors.allowed-origins}")
    lateinit var allowedOrigins: List<String>
}