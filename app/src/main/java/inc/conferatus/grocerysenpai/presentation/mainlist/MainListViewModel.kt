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
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.byName
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.filterCategories
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.sortCategories
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
    val currentGroceries = groceryRepository.getCurrentGroceriesStream()

    var textInput by mutableStateOf(""); private set
    var isInputValidated by mutableStateOf(false); private set
    var inputCategory: CategoryItem? by mutableStateOf(null); private set
    var suggestedCategories: List<String> by mutableStateOf(emptyList()); private set

    init {
        validateInput()
    }

    fun addItem() {
        if (inputCategory == null) {
            throw RuntimeException("насрал") // todo норм исключение сделать
        }

        val newItem = GroceryItem(
            category = inputCategory!!,
            description = "no description",
            amount = 1,
            amountPostfix = "шт",
        )

        viewModelScope.launch {
            groceryRepository.insertGrocery(newItem)
        }

        updateInput("")
    }

    fun removeItem(item: GroceryItem) {
        viewModelScope.launch {
            groceryRepository.deleteGrocery(item)
        }
    }

    fun buyItem(item: GroceryItem) { // todo maybe rename??
        viewModelScope.launch {
            groceryRepository.updateGroceryBoughtDate(item, Date.from(Instant.now()))
        }
    }

    fun updateInput(newItemInput: String) {
        textInput = newItemInput
        validateInput()
    }

    private fun validateInput() {
        inputCategory = categoriesListSingletone.categories.byName(textInput);
        isInputValidated = inputCategory != null

        viewModelScope.launch {
            suggestedCategories = categoriesListSingletone.categories
                .filterCategories(inputBeginning = textInput)
                .sortCategories(limit = 12)
                .map { it.name }
        }
    }
}
