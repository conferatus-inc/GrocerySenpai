package org.example.mainbackend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.example.mainbackend.dto.ProductDto
import java.time.OffsetDateTime

@Entity
data class Product(
    @Id
    @GeneratedValue
    val id: Long? = null,
    @Column(nullable = false)
    val category: String,
    @Column(nullable = false)
    val boughtOn: OffsetDateTime,
    //    @JoinColumn(nullable = false)
    @ManyToOne
    val user: User?,
) {
    constructor(productDto: ProductDto) : this(
        category = productDto.category,
        boughtOn = productDto.boughtOn,
        user = null,
    )
}
