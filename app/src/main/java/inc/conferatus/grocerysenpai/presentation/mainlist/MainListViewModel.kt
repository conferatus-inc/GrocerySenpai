package inc.conferatus.grocerysenpai.presentation.mainlist

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import inc.conferatus.grocerysenpai.model.CategoriesListSingleton
import inc.conferatus.grocerysenpai.model.items.CategoryItem
import inc.conferatus.grocerysenpai.model.items.GroceryItem
import inc.conferatus.grocerysenpai.model.repository.GroceryRepository
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.byName
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.filterCategories
import inc.conferatus.grocerysenpai.model.util.CategoriesUtils.Companion.sortCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
// todo часть логики отсюда будет в модели
class MainListViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    val categoriesListSingleton: CategoriesListSingleton
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
                .filterCategories(inputBeginning = textInput)
                .sortCategories(limit = 7, input = textInput)
                .map { it.name }
        }
    }

    // todo move to utils
    // и вообще все переписать чтобы нормально было
    // временное решение за день до презы

    companion object {
//        private val mapper = jacksonObjectMapper()
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        // todo это вообще синглтон будет
        private const val BASE_URL = "http://localhost:8887"
        val api: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }

    // просто ужас
    public val historyConvertedToSend = groceryRepository.getHistoryGroceriesStream()
        .map { it.map {entry -> BoughtItemDto(category = entry.category.name, boughtOn = entry.bought!!.format(formatter))} }
//        .map { SendHistoryDto(it) }

    var suggestedProds: List<SuggestedItemDto> by mutableStateOf(emptyList()); private set

    public fun getSuggested(historyDto: SendHistoryDto) {
        viewModelScope.launch {
            try {
                val id = api.postTask(historyDto)
                val res = api.getTask(id)
                suggestedProds = res.items
            } catch (e: Exception) {suggestedProds = listOf(SuggestedItemDto(e.message!!, ""))
            }
        }
    }
}
