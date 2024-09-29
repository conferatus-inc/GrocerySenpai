package inc.conferatus.grocerysenpai.presentation.shoppinglist

import androidx.lifecycle.ViewModel
import inc.conferatus.grocerysenpai.data.GroceryListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger

class GroceryListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GroceryListUiState())
    val uiState = _uiState.asStateFlow()

    fun newGavna() {
        _uiState.update {
            it.copy(
                groceryListItems = it.groceryListItems.plus(GroceryListItem()),
                counter = it.counter + 1
            )
        }
    }

    fun clear() {
        _uiState.update {
            it.copy(
                groceryListItems = emptyList(),
                counter = 0
            )
        }
    }
}
