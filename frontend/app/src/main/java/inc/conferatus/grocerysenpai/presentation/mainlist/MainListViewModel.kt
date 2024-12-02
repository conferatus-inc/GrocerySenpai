package inc.conferatus.grocerysenpai.presentation.mainlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import inc.conferatus.grocerysenpai.api.AuthService
import inc.conferatus.grocerysenpai.api.BackendApi
import inc.conferatus.grocerysenpai.api.ChangeDto
import inc.conferatus.grocerysenpai.api.ChangeType
import inc.conferatus.grocerysenpai.api.ProductDto
import inc.conferatus.grocerysenpai.api.SuggestedItemDto
import inc.conferatus.grocerysenpai.model.CategoriesListSingleton
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.predict
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.byName
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.filterCategories
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
// todo часть логики отсюда будет в модели
class MainListViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    val categoriesListSingleton: CategoriesListSingleton
) : ViewModel() {
    val currentGroceries = groceryRepository.getCurrentGroceriesStream()
    val historyGroceries = groceryRepository.getHistoryGroceriesStream()

    var textInput by mutableStateOf(""); private set
    var isInputValidated by mutableStateOf(false); private set
    var inputCategory: CategoryItem? by mutableStateOf(null); private set

    var suggestedCategories: List<String> by mutableStateOf(emptyList()); private set

    init {
        validateInput()
    }

    private suspend fun fetchDataFromBackend() {
        val changes =
            BackendApi.changesApi.getChanges(AuthService.accessToken, AuthService.lastUpdatedTime)


        println(AuthService.lastUpdatedTime)

        AuthService.lastUpdatedTime = Instant.now()

        println("FNSDJFBNABK:SFBAKSF")

        for (change in changes) {
            when (change.changeType) {
                ChangeType.ADD -> {
                    if (change.product.boughtOn == Instant.EPOCH) {
                        change.product.boughtOn = null
                    }
                    viewModelScope.launch { groceryRepository.insertGrocery(change.product.toGrocery()) }
                }

                ChangeType.EDIT -> {
                    if (change.product.boughtOn == Instant.EPOCH) {
                        change.product.boughtOn = null
                    }
                    viewModelScope.launch { groceryRepository.updateGrocery(change.product.toGrocery()) }
                }

                ChangeType.DELETE -> {
                    if (change.product.boughtOn == Instant.EPOCH) {
                        change.product.boughtOn = null
                    }
                    viewModelScope.launch { groceryRepository.deleteGrocery(change.product.toGrocery()) }
                }
            }
            println(change)
        }
    }

    suspend fun startDataFetching() {
        while (true) {
            fetchDataFromBackend()
            delay(3000)
        }
    }

    suspend fun addItem() {
        val item = BackendApi.changesApi.makeChanges(
            AuthService.accessToken,
            listOf(
                ChangeDto(
                    ProductDto(
                        category = inputCategory!!.id.toString(),
                        boughtOn = Instant.EPOCH,
                        isActive = true
                    ),
                    changeType = ChangeType.ADD,
                    changeTime = Instant.now()
                )
            )
        ).first()

        if (inputCategory == null) {
            throw RuntimeException("насрал") // todo норм исключение сделать
        }

        val newItem = GroceryItem(
            id = item.id!!.toInt(),
            category = inputCategory!!,
            description = "",
            amount = 1,
            amountPostfix = "",
        )

        viewModelScope.launch {
            groceryRepository.insertGrocery(newItem)
        }

        updateInput("")
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

    suspend fun buyItem(item: GroceryItem) { // todo maybe rename??
        BackendApi.changesApi.makeChanges(
            AuthService.accessToken,
            listOf(
                ChangeDto(
                    ProductDto(
                        id = item.id.toLong(),
                        category = item.category.id.toString(),
                        boughtOn = Instant.now(),
                        isActive = false
                    ),
                    changeType = ChangeType.EDIT,
                    changeTime = Instant.now()
                )
            )
        )

        viewModelScope.launch {
            groceryRepository.updateGroceryBoughtDate(item, ZonedDateTime.now())
        }
    }

    fun updateInput(newItemInput: String) {
        textInput = newItemInput
        validateInput()
    }

    private fun validateInput() {
        inputCategory = categoriesListSingleton.categories.byName(textInput)
        isInputValidated = inputCategory != null

        viewModelScope.launch {
            suggestedCategories = categoriesListSingleton.categories
                .filterCategories(inputBeginning = textInput, limit = 10)
                .map { it.name }
        }
    }

    data class BeforePredictor(
        val category: CategoryItem,
        val dayNumbers: List<Long>,
        val earliestDay: ZonedDateTime
    )

    data class AfterPredictor(
        val category: CategoryItem,
        val daysBeforeNextBuy: Long,
        val earliestDay: ZonedDateTime
    )

    fun getSuggested(history: List<GroceryItem>): List<SuggestedItemDto> {
        return categoriesListSingleton.categories
            .asSequence()
            .map { category -> history.filter { it.category.name == category.name } }
            .filter { it.isNotEmpty() }
            .map { list ->
                val earliestBuy = list.minOf { it.bought!! }
                val days = list.map { earliestBuy.until(it.bought!!, ChronoUnit.DAYS) }
                BeforePredictor(list[0].category, days, earliestBuy)
            }
            .map { AfterPredictor(it.category, predict(it.dayNumbers), it.earliestDay) }
            .map {
                SuggestedItemDto(
                    it.category.name,
                    it.earliestDay.plusDays(it.daysBeforeNextBuy)
                )
            }
            .onEach { println(it) }
            .sortedBy { it.nextBuy }
            .toList()
    }

    fun genFakes() {
        viewModelScope.launch {
            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Вода")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(5)
                )
            )
            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Вода")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(3)
                )
            )
            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Вода")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(1)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Хлеб")!!,
                    "",
                    3,
                    "",
                    ZonedDateTime.now().minusDays(14)
                )
            )
            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Хлеб")!!,
                    "",
                    3,
                    "",
                    ZonedDateTime.now().minusDays(9)
                )
            )
            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Хлеб")!!,
                    "",
                    3,
                    "",
                    ZonedDateTime.now().minusDays(7)
                )
            )
            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Хлеб")!!,
                    "",
                    3,
                    "",
                    ZonedDateTime.now().minusDays(6)
                )
            )
            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Хлеб")!!,
                    "",
                    3,
                    "",
                    ZonedDateTime.now().minusDays(1)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Лимонад")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(58)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Лимонад")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(29)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Лимонад")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(80)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Лимонад")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(5)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Пиво")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(29)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Пиво")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(24)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Пиво")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(18)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Пиво")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(12)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Пиво")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(7)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Пиво")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(2)
                )
            )



            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Сыр")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(10)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Сыр")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(13)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Сыр")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(16)
                )
            )

            groceryRepository.insertGrocery(
                GroceryItem(
                    (53245..624574252).random(),
                    categoriesListSingleton.categories.byName("Сыр")!!,
                    "",
                    1,
                    "",
                    ZonedDateTime.now().minusDays(19)
                )
            )
        }
    }


//    public fun getByQr(data: String) {
//        viewModelScope.launch {
//            try {
//                val products = api.getQrData(data)
//            } catch (e: Exception) {
//                suggestedProds = listOf(SuggestedItemDto(e.message!!, ""))
//            }
//        }
//    }
}
