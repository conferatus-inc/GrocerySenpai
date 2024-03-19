package inc.conferatus.grocerysenpai.presentation.mainlist

import inc.conferatus.grocerysenpai.model.items.CategoryItem

// todo вот это стоит приделывать или нет пока хз
data class MainListUiState(
    val textInput: String = "",
    val isInputValidated: Boolean = false,
    val currentCategory: CategoryItem? = null,
    val suggestedCategories: List<CategoryItem> = listOf()
)
