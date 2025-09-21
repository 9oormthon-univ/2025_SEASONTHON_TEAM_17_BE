package shop.maeum.global.swagger

import io.swagger.v3.oas.models.PathItem
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CustomOpenApi(
    private val logOutOperationProvider: LogOutOperationProvider,
    @Value("\${spring.security.logout.url}")
    private val logOutUrl: String
) {

    @Bean
    fun addLogOutApi(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.paths.addPathItem(
                logOutUrl,
                PathItem().post(logOutOperationProvider.getLogOutOperation())
            )
        }
    }
}