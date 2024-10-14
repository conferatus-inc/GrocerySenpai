package inc.conferatus.grocerysenpai.presentation.mainlist

import inc.conferatus.grocerysenpai.model.GroceryItem

data class MainListUiState(
    val groceryItems: List<GroceryItem> = emptyList()
)
