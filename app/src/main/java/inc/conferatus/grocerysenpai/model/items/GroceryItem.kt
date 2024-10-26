package inc.conferatus.grocerysenpai.model.items

import java.time.LocalDate

data class GroceryItem(
    val id: Int = 0,
    val category: CategoryItem,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: LocalDate? = null
)
