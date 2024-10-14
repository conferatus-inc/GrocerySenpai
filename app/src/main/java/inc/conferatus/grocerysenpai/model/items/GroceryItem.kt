package inc.conferatus.grocerysenpai.model.items

import inc.conferatus.grocerysenpai.data.entity.CategoryEntity

data class GroceryItem(
    val id: Int,
    val category: Category,
    val description: String,
    val amount: Int,
    val amountPostfix: String
)
