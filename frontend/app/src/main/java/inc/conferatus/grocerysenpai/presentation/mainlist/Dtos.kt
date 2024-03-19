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
    val products: List<ProductDto>,
    val date: String
)
