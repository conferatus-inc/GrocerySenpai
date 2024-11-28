package inc.conferatus.grocerysenpai.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BackendApi {
    private const val URL = "http://158.160.96.57:8080/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create(),
                ),
            )
            .build()
    }

    val productsApi: ProductsApiService by lazy {
        retrofit.create(ProductsApiService::class.java)
    }

    val qrApi: QrApiService by lazy {
        retrofit.create(QrApiService::class.java)
    }

    val loginApi: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}