package inc.conferatus.grocerysenpai.presentation.mainlist

data class SuggestedItemDto(
    val category: String,
    val nextBuy: String
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

data class ProductDto(
    val category: String,
    val amount: Int
)

data class ProductsDto(
    val products: Array<ProductDto>,
    val date: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductsDto

        if (!products.contentEquals(other.products)) return false
        return date == other.date
    }

    override fun hashCode(): Int {
        var result = products.contentHashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}

