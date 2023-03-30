package io.cws.item.controller

import io.cws.item.dto.ChangeItemForm
import io.cws.item.dto.CreateItemForm
import io.cws.item.entity.Item
import io.cws.item.service.ItemService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class ItemController (
    private val itemService: ItemService
) {

    @GetMapping("/api/v1/items/{id}")
    fun findById(@PathVariable("id") id: Long): Item {
        return itemService.findById(id)
    }

    @GetMapping("/api/v1/items")
    fun findAll(): List<Item> {
        return itemService.findAll()
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/items")
    fun createItem(@RequestBody form: CreateItemForm): Long {
        return itemService.createItem(form)
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/api/v1/items")
    fun changeItem(@RequestBody form: ChangeItemForm): Long {
        return itemService.changeItem(form)
    }
}