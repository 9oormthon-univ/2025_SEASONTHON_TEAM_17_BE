package shop.maeum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync

@EnableFeignClients
@SpringBootApplication(
    exclude = [org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration::class]
)
@EnableAsync
class MaeumApplication

fun main(args: Array<String>) {
    runApplication<MaeumApplication>(*args)
}
