package shop.maeum.domain.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.security.cors")
class CorsConfig {
    var allowedOrigins: List<String> = emptyList()
    var allowedPaths: List<String> = emptyList()
    var allowedHeaders: List<String> = listOf("*")
    var allowedMethods: List<String> = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
}

