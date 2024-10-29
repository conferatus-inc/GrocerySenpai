package inc.conferatus.grocerysenpai.presentation.mainlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import kotlinx.coroutines.launch
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
