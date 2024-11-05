package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inc.conferatus.grocerysenpai.presentation.mainlist.ApiService
import inc.conferatus.grocerysenpai.presentation.mainlist.ProductsDto
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class QrScannerViewModel @Inject constructor(
) : ViewModel() {
    var lastScanResult by mutableStateOf<ProductsDto?>(null); private set //
    companion object {
        private const val BASE_URL = "http://localhost:8887"
        val api: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }

    fun sendRequest(str: String) {
        viewModelScope.launch {
            lastScanResult = api.getQrData(str)
        }
    }
}