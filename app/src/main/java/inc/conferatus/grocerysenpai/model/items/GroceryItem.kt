package inc.conferatus.grocerysenpai.model.items

import java.util.Date

sealed class GroceryItem() // todo насрал, мб забить и сделать адекватно

data class CurrentGroceryItem(
    val id: Int,
    val category: CategoryItem,
    val description: String,
    val amount: Int,
    val amountPostfix: String
) : GroceryItem()

data class HistoryGroceryItem(
    val id: Int,
    val category: CategoryItem,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: Date
) : GroceryItem()
