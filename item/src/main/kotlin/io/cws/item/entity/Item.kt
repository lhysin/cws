package io.cws.item.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "TBL_ITEM")
class Item(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var price: BigDecimal,

    @Column(nullable = true)
    @CreatedDate
    val createdDate: LocalDateTime? = null,

    @Column(nullable = true)
    @LastModifiedDate
    val lastModifiedDate: LocalDateTime? = null,
)