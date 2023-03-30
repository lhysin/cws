package io.cws.item.dto

import java.math.BigDecimal

class ChangeItemForm (
    val id: Long,
    val name: String,
    val price: BigDecimal,
) {
}