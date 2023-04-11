package io.cws.item.controller

import io.cws.item.ItemApiApplication
import io.cws.item.dto.ChangeItemForm
import io.cws.item.dto.CreateItemForm
import io.cws.item.entity.Item
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTests (

    @Autowired
    var webTestClient: WebTestClient
) {

    @Test
    fun should_createItemAndFindById() {

        val createdId = webTestClient.post()
            .uri("/api/v1/items")
            .bodyValue(
                CreateItemForm(
                    name = "상품명",
                    price = BigDecimal(1000)
                )
            ).exchange()
            .expectStatus()
            .isCreated
            .expectBody(Long::class.java)
            .returnResult()
            .responseBody
            ?: -1

        val item = webTestClient.get()
            .uri("/api/v1/items/{id}", createdId)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<Item>()
            .returnResult()
            .responseBody

        logger.debug("found item name : {}", item?.name)

        webTestClient.put()
            .uri("/api/v1/items")
            .bodyValue(
                ChangeItemForm(
                    id = createdId,
                    name = "변경된 상품명",
                    price = BigDecimal(9000)
                )
            ).exchange()
            .expectStatus()
            .isAccepted

        val itemList = webTestClient.get()
            .uri("/api/v1/items")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<List<Item>>()
            .returnResult()
            .responseBody

        itemList?.forEach { item -> logger.debug("found item name : {}", item.name) }

    }

}