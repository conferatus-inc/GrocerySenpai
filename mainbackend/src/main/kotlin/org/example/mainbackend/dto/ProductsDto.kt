package org.example.mainbackend.dto

import org.example.mainbackend.model.Product
import java.time.OffsetDateTime

data class ProductsDto(
    val items: List<ProductDto>,
)

data class ProductDto(
    val id: Long?,
    val category: String,
    val boughtOn: OffsetDateTime,
    val isActive: Boolean,
    val user: SimpleUserDto?,
) {
    constructor(product: Product) : this(
        product.id!!,
        product.category,
        product.boughtOn,
        product.isActive,
        if (product.user != null) SimpleUserDto(product.user!!) else null,
    )
}
