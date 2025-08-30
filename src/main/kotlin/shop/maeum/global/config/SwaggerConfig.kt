package shop.maeum.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class SwaggerConfig(
    converter: MappingJackson2HttpMessageConverter
) {

    init {
        val supportedMediaTypes = converter.supportedMediaTypes.toMutableList()
        supportedMediaTypes.add(MediaType("application", "octet-stream"))
        converter.supportedMediaTypes = supportedMediaTypes
    }

    @Bean
    fun maeumAPI(): OpenAPI {
        val info = Info()
            .title("마음 API")
            .description("마음 API 명세서")
            .version("1.0.0")

        val jwtSchemeName = "JWT TOKEN"
        val securityRequirement = SecurityRequirement().addList(jwtSchemeName)
        val components = Components()
            .addSecuritySchemes(
                jwtSchemeName,
                SecurityScheme()
                    .name(jwtSchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )

        val localServer = Server()
            .url("https://maeum.shop")
            .description("Local Server")

        return OpenAPI()
            .info(info)
            .servers(listOf(localServer))
            .addSecurityItem(securityRequirement)
            .components(components)
    }
}
