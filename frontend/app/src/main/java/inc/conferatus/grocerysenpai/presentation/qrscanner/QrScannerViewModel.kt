package inc.conferatus.grocerysenpai.presentation.qrscanner

import androidx.lifecycle.ViewModel
import inc.conferatus.grocerysenpai.api.QrProductsDto
import javax.inject.Inject

@Deprecated(message = "Больше не используем")
class QrScannerViewModel @Inject constructor(
) : ViewModel() {

//    var lastScanResult by mutableStateOf<ProductsDto?>(null); private set //
//    companion object {
//        private const val BASE_URL = "http://192.168.0.102:8000"
//        val api: ApiService by lazy {
//            val retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//            retrofit.create(ApiService::class.java)
//        }
//    }

    fun sendRequest(str: String): QrProductsDto {
//        println("XDDXDD")
//        println(str)
//        viewModelScope.launch {
//            try {
//                lastScanResult = api.getQrData(str)
//            } catch (_: Exception) {
//                println("ABOBA")
//            }
//        }
//        println(lastScanResult.toString())
//        return lastScanResult ?: ProductsDto(emptyList(), "")
        return QrProductsDto(emptyList(), "")
    }
}