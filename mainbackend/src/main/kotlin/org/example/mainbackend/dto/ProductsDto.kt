package org.example.mainbackend.dto

import org.example.mainbackend.model.Product
import java.time.OffsetDateTime

data class ProductsDto(
    val items: List<ProductDto>,
)

data class ProductDto(
    val category: String,
    val boughtOn: OffsetDateTime,
) {
    constructor(product: Product) : this(product.category, product.boughtOn)
}
