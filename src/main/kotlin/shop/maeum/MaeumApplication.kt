package shop.maeum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MaeumApplication

fun main(args: Array<String>) {
    runApplication<MaeumApplication>(*args)
}
