package inc.conferatus.grocerysenpai.api

import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

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
    val amount: Double,
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
    var boughtOn: Instant?,
    var isActive: Boolean,
    val user: SimpleUserDto? = null,
) {
    fun toGrocery(): GroceryItem {
        return GroceryItem(
            id?.toInt() ?: (53245..624574252).random(),
            CategoryItem(
//                id = (53245..624574252).random(),
                id = category.toInt(),
                name = category
            ),
            description = "",
            amount = 1,
            amountPostfix = "",
            bought = if (boughtOn == null) null
            else ZonedDateTime.from(boughtOn!!.atZone(ZoneId.systemDefault()))
        )
    }
}

data class SimpleUserDto(
    val id: Long,
    val username: String,
)

enum class Role {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_ROOT,
}

data class ChangeDto(
    val product: ProductDto,
    val changeType: ChangeType,
    val changeTime: Instant,
)

enum class ChangeType {
    ADD,
    EDIT,
    DELETE,
}
