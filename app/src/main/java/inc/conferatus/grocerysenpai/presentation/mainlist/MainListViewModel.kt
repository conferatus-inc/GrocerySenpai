package inc.conferatus.grocerysenpai.presentation.mainlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import inc.conferatus.grocerysenpai.model.GroceryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//@HiltViewModel
//class GroceryListViewModel @Inject constructor(
//
//): ViewModel() {
// todo часть логики отсюда будет в модели
class MainListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainListUiState())
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
                groceryItems = it.groceryItems.plus(GroceryItem(name = itemInput))
            )
        }
        updateItemInput("")
    }

    fun removeItem(item: GroceryItem) {
        _uiState.update { state ->
            state.copy(
                groceryItems = state.groceryItems.filter { it !== item } // todo потом норм будет это просто гуйню тестить
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
