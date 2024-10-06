package org.example.mainbackend.dto

import java.time.OffsetDateTime

data class ProductsDto(
    val items: List<Product>,
)

data class Product(
    val category: String,
    val boughtOn: OffsetDateTime,
)
