package io.cws.item.service

import io.cws.item.dto.ChangeItemForm
import io.cws.item.dto.CreateItemForm
import io.cws.item.entity.Item
import io.cws.item.producer.ItemProducer
import io.cws.item.repository.ItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ItemService (
    private val itemRepository: ItemRepository,
    private val itemProducer: ItemProducer
){
    fun findById(id: Long): Item {
        return itemRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun findAll(): List<Item> {
        return itemRepository.findAll()
    }

    fun createItem(form: CreateItemForm): Long {
        val item = Item(
            name = form.name,
            price = form.price
        )

        itemRepository.save(item)

        return item.id ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun changeItem(form: ChangeItemForm): Long {
        val item = this.findById(form.id)
        item.name = form.name
        item.price = form.price

        itemRepository.save(item)

        itemProducer.sendItemInformation(item)

        return form.id
    }


}