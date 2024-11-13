package inc.conferatus.grocerysenpai.api

import java.time.ZonedDateTime
import java.util.Date

data class SuggestedItemDto(
    val category: String,
    val nextBuy: ZonedDateTime
)

data class BoughtItemDto(
    val category: String,
    val boughtOn: String
)

data class SendHistoryDto(
    val items: List<BoughtItemDto>
)

data class SuggestionsDto(
    val items: List<SuggestedItemDto>
)

data class QrProductDto(
    val category: String,
    val amount: Int,
)

data class QrProductsDto(
    val products: List<QrProductDto>,
    val date: String,
)

data class ProductsDto(
    val products: List<ProductDto>,
)

data class ProductDto(
    val id: Long? = null,
    val category: String,
    var boughtOn: Date,
    var isActive: Boolean,
    val user: SimpleUserDto? = null,
)

data class SimpleUserDto(
    val id: Long,
    val username: String,
)

enum class Role {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_ROOT,
}
