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
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.filterCategories
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.sortCategories
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
) : ViewModel() {
    val historyGroceries = groceryRepository.getHistoryGroceriesStream()

    fun addItem(item: GroceryItem) {
        viewModelScope.launch {
            groceryRepository.insertGrocery(
                item.copy(
                    id = 0,
                    bought = null
                )
            )
        }
    }

    fun removeItem(item: GroceryItem) {
        viewModelScope.launch {
            groceryRepository.deleteGrocery(item)
        }
    }
}
