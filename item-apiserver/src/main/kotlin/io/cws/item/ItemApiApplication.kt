package io.cws.item

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ItemApiApplication

fun main(args: Array<String>) {
    runApplication<ItemApiApplication>(*args)
}
