package inc.conferatus.grocerysenpai.presentation.shoppinglist

import inc.conferatus.grocerysenpai.data.GroceryListItem

data class GroceryListUiState(
    val groceryListItems: List<GroceryListItem> = emptyList()
)
