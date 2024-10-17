package inc.conferatus.grocerysenpai.presentation.mainlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inc.conferatus.grocerysenpai.model.CategoriesListSingletone
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.repository.CategoryRepository
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import javax.inject.Inject

//@HiltViewModel
// todo часть логики отсюда будет в модели
class MainListViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val categoryRepository: CategoryRepository,
    /*private*/ val categoriesListSingletone: CategoriesListSingletone
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainListUiState())
    val uiState = _uiState.asStateFlow()

    var itemInput by mutableStateOf("")
        private set

    var isInputValidated by mutableStateOf(false)
        private set

    init {
        tryValidateItemInput()

        viewModelScope.launch {
            _uiState.update { // todo хрень
                it.copy(
                    groceryItems = groceryRepository.getAllGroceriesStream().first()
                )
            }

            val category = categoryRepository.getCategoryStreamByName("Beer").first()
            if (category == null) {
                categoryRepository.insertCategory(CategoryItem(name = "Beer")) // сделать адекватно препопулате
            }
        }

    }

    fun addItem() {
        viewModelScope.launch {
            val category = categoryRepository.getCategoryStreamByName("Beer").first()
            assert(category != null)

            val newItem = GroceryItem(
                category = category!!,
                description = itemInput,
                amount = 2,
                amountPostfix = "kg",
                bought = null
            )

            _uiState.update {
                it.copy(
                    groceryItems = it.groceryItems.plus(newItem)
                )
            }

            groceryRepository.insertGrocery(newItem)
        }

        updateItemInput("")
    }

    fun removeItem(item: GroceryItem) {
        viewModelScope.launch {
            groceryRepository.updateGroceryBoughtDate(item, Date.from(Instant.now()))
        }
        _uiState.update { state ->
            state.copy(
                groceryItems = state.groceryItems.filter { it !== item }
            )
        }
    }

    fun updateItemInput(newItemInput: String) {
        itemInput = newItemInput
        tryValidateItemInput()
    }

    private fun tryValidateItemInput() {
        isInputValidated = itemInput.isNotBlank()
    }

//    fun clear() {
//        _uiState.update {
//            it.copy(
//                groceryListItems = emptyList(),
//            )
//        }
//    }
}
