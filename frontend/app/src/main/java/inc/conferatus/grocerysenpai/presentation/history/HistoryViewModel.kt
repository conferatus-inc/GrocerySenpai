package inc.conferatus.grocerysenpai.presentation.mainlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import inc.conferatus.grocerysenpai.api.AuthService
import inc.conferatus.grocerysenpai.api.BackendApi
import inc.conferatus.grocerysenpai.api.ChangeDto
import inc.conferatus.grocerysenpai.api.ChangeType
import inc.conferatus.grocerysenpai.api.ProductDto
import inc.conferatus.grocerysenpai.model.CategoriesListSingleton
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    val categoriesListSingleton: CategoriesListSingleton
) : ViewModel() {
    val historyGroceries = groceryRepository.getHistoryGroceriesStream()

    suspend fun addItem(item: GroceryItem) {
        println(item.toString())
        println()
//
        BackendApi.changesApi.makeChanges(
            AuthService.accessToken,
            listOf(
                ChangeDto(
                    ProductDto(
                        id = item.id.toLong(),
                        category = item.category.id.toString(),
                        boughtOn = Instant.EPOCH,
                        isActive = true
                    ),
                    changeType = ChangeType.EDIT,
                    changeTime = Instant.now()
                )
            )
        ).first()

        viewModelScope.launch {
            groceryRepository.insertGrocery(
                item.copy(
                    bought = null // TODO
                )
            )
        }
    }

    suspend fun addNewItem(item: GroceryItem) {
        println(item.toString())
        println()

        val category = categoriesListSingleton.categories.find { it.name == item.category.name }!!

        val newItem = BackendApi.changesApi.makeChanges(
            AuthService.accessToken,
            listOf(
                ChangeDto(
                    ProductDto(
                        category = category.id.toString(),
                        boughtOn = Instant.now(),
                        isActive = false
                    ),
                    changeType = ChangeType.ADD,
                    changeTime = Instant.now()
                )
            )
        ).first()

        viewModelScope.launch {
            groceryRepository.insertGrocery(
                item.copy(
                    id = newItem.id!!.toInt(),
                    category = CategoryItem(
                        id = category.id,
                        name = category.name
                    ),
                    bought = ZonedDateTime.now() // TODO
                )
            )
        }
    }

    suspend fun removeItem(item: GroceryItem) {
        BackendApi.changesApi.makeChanges(
            AuthService.accessToken,
            listOf(
                ChangeDto(
                    ProductDto(
                        id = item.id.toLong(),
                        category = item.category.id.toString(),
                        boughtOn = Instant.EPOCH,
                        isActive = true
                    ),
                    changeType = ChangeType.DELETE,
                    changeTime = Instant.now()
                )
            )
        )

        viewModelScope.launch {
            groceryRepository.deleteGrocery(item)
        }
    }
}
