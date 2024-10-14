package inc.conferatus.grocerysenpai.presentation.mainlist

import inc.conferatus.grocerysenpai.model.items.GroceryItem

data class MainListUiState(
    val groceryItems: List<GroceryItem> = emptyList()
)
