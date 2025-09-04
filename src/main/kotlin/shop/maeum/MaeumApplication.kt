package shop.maeum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication(
    exclude = [org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration::class]
)
class MaeumApplication

fun main(args: Array<String>) {
    runApplication<MaeumApplication>(*args)
}
