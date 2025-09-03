package shop.maeum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(
    exclude = [org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration::class]
)
@EnableAsync
class MaeumApplication

fun main(args: Array<String>) {
    runApplication<MaeumApplication>(*args)
}
