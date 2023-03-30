package io.cws.item

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ItemApplication

fun main(args: Array<String>) {
    runApplication<ItemApplication>(*args)
}
