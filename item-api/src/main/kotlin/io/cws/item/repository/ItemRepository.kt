package io.cws.item.repository

import io.cws.item.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository<Item, Long> {
}