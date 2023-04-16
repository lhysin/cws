package io.cws.item.producer

import com.fasterxml.jackson.databind.ObjectMapper
import io.cws.item.entity.Item
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class ItemProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val mapper : ObjectMapper,

    @Value("\${spring.kafka.template.default-topic}")
    private val topic: String
) {

    fun sendItemInformation(item : Item) {
        val message = mapper.writeValueAsString(item)
        kafkaTemplate.send(topic, message)
        logger.info("\n[topic] : $topic [message] : $message")
    }
}