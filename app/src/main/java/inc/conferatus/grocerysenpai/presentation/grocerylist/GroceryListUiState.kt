package inc.conferatus.grocerysenpai.presentation.grocerylist

import inc.conferatus.grocerysenpai.data.GroceryListItem

data class GroceryListUiState(
    val groceryListItems: List<GroceryListItem> = emptyList()
)
