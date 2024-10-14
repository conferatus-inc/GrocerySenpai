package inc.conferatus.grocerysenpai.model.items

import inc.conferatus.grocerysenpai.data.entity.CategoryEntity
import java.util.Date

data class HistoryGroceryItem(
    val id: Int,
    val category: Category,
    val description: String,
    val amount: Int,
    val amountPostfix: String,
    val bought: Date
)
