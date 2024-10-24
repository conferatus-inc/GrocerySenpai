package inc.conferatus.grocerysenpai.presentation.mainlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import inc.conferatus.grocerysenpai.model.CategoriesListSingletone
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.repository.CategoryRepository
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@HiltViewModel
// todo часть логики отсюда будет в модели
class MainListViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    val categoriesListSingletone: CategoriesListSingletone
) : ViewModel() {
    val currentGroceries = groceryRepository.getAllGroceriesStream()

    // todo все нижележащее - в uistate???

    var itemInput by mutableStateOf("")
        private set

    var isInputValidated by mutableStateOf(false)
        private set

    var currentCategory: CategoryItem? by mutableStateOf(null)
        private set

    var suggestedCategories: List<String> by mutableStateOf(emptyList())
        private set

    init {
        tryValidateItemInput()
    }

    fun addItem() {
        if (currentCategory == null) {
            throw RuntimeException("насрал") // todo норм исключение сделать
        }

        val newItem = GroceryItem(
            category = currentCategory!!, // todo мб будет падать хз
            description = "no description yet",
            amount = 1,
            amountPostfix = "шт",
        )

        viewModelScope.launch {
            groceryRepository.insertGrocery(newItem)
        }

        updateItemInput("")
    }

    fun removeItem(item: GroceryItem) {
        viewModelScope.launch {
            groceryRepository.updateGroceryBoughtDate(item, Date.from(Instant.now()))
        }
    }

    fun updateItemInput(newItemInput: String) {
        itemInput = newItemInput
        tryValidateItemInput()
    }

    private fun tryValidateItemInput() {
        currentCategory = CategoriesUtils.byName(itemInput, categoriesListSingletone.categories);
        isInputValidated = currentCategory != null

        viewModelScope.launch {
            suggestedCategories = CategoriesUtils.sort(
                categories = CategoriesUtils.filter(
                    inputBeginning = itemInput,
                    categories = categoriesListSingletone.categories
                ),
                limit = 10
            ).map { it.name }
        }
    }
}
