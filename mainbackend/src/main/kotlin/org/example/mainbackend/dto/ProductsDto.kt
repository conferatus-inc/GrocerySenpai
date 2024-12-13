package org.example.mainbackend.dto

import org.example.mainbackend.model.Product
import java.time.Instant

data class ProductsDto(
    val products: List<ProductDto>,
)

data class ProductDto(
    val id: Long?,
    val category: String,
    val boughtOn: Instant,
    val isActive: Boolean,
    val user: SimpleUserDto?,
    val isDeleted: Boolean?,
)

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        category = category,
        boughtOn = boughtOn,
        isActive = isActive,
        user = null,
    )
}
