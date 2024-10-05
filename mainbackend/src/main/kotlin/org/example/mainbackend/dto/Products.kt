package org.example.mainbackend.dto

import java.time.LocalDateTime

data class Products(
    val items: List<Product>,
)

data class Product(
    val category: String,
    val boughtOn: LocalDateTime,
)
