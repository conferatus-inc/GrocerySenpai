package org.example.mainbackend.dto

import org.example.mainbackend.model.Product
import java.util.*

data class ProductsDto(
    val items: List<ProductDto>,
)

data class ProductDto(
    val id: Long?,
    val category: String,
    // @DateTimeFormat(style = "dd-MM-yyyy")
    
    val boughtOn: Date, // 2024-03-27T22:06:14.969012300+07:00
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
