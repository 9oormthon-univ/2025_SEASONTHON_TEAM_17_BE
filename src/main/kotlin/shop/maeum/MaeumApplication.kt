package shop.maeum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration::class]
)
class MaeumApplication

fun main(args: Array<String>) {
    runApplication<MaeumApplication>(*args)
}
