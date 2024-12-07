package org.example.mainbackend.dto

import org.example.mainbackend.model.Product
import java.util.Date

data class ProductsDto(
    val products: List<ProductDto>,
)

data class ProductDto(
    val id: Long?,
    val category: String,
    val boughtOn: Date,
    val isActive: Boolean,
    val user: SimpleUserDto?,
    val isDeleted: Boolean?,
) {
    constructor(product: Product) : this(
        product.id!!,
        product.category,
        product.boughtOn,
        product.isActive,
        if (product.user != null) SimpleUserDto(product.user!!) else null,
        product.isDeleted,
    )
}

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        category = category,
        boughtOn = boughtOn,
        isActive = isActive,
        user = null,
    )
}
