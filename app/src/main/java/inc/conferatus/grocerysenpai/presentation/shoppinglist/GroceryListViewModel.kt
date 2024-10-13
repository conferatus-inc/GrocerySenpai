package inc.conferatus.grocerysenpai.presentation.shoppinglist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import inc.conferatus.grocerysenpai.data.GroceryListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//@HiltViewModel
//class GroceryListViewModel @Inject constructor(
//
//): ViewModel() {
class GroceryListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GroceryListUiState())
    val uiState = _uiState.asStateFlow()

    var itemInput by mutableStateOf("")
        private set

    fun addItem() {
        _uiState.update {
            it.copy(
                groceryListItems = it.groceryListItems.plus(GroceryListItem(name = itemInput))
            )
        }
        itemInput = ""
    }

    fun removeItem(item: GroceryListItem) {
        _uiState.update {
            it.copy(
                groceryListItems = it.groceryListItems.filter { it !== item } // todo потом норм будет это просто гуйню тестить
            )
        }
    }

    fun updateUserItemInput(newItemInput: String) {
         itemInput = newItemInput
    }

    fun clear() {
        _uiState.update {
            it.copy(
                groceryListItems = emptyList(),
            )
        }
    }
}
