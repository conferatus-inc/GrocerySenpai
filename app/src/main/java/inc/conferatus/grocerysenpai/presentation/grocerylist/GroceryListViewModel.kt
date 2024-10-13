package inc.conferatus.grocerysenpai.presentation.grocerylist

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
// todo часть логики отсюда будет в модели
class GroceryListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GroceryListUiState())
    val uiState = _uiState.asStateFlow()

    var itemInput by mutableStateOf("")
        private set

    var itemInputValidate by mutableStateOf(false)
        private set

    init {
        validateItemInput()
    }

    fun addItem() {
        _uiState.update {
            it.copy(
                groceryListItems = it.groceryListItems.plus(GroceryListItem(name = itemInput))
            )
        }
        updateItemInput("")
    }

    fun removeItem(item: GroceryListItem) {
        _uiState.update { state ->
            state.copy(
                groceryListItems = state.groceryListItems.filter { it !== item } // todo потом норм будет это просто гуйню тестить
            )
        }
    }

    fun updateItemInput(newItemInput: String) {
        itemInput = newItemInput
        validateItemInput()
    }

    private fun validateItemInput() {
        itemInputValidate = itemInput.isNotBlank()
    }

//    fun clear() {
//        _uiState.update {
//            it.copy(
//                groceryListItems = emptyList(),
//            )
//        }
//    }
}
