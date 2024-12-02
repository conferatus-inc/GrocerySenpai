package inc.conferatus.grocerysenpai.api

import retrofit2.http.GET
import retrofit2.http.Header

interface ProductsApiService {
    @GET("products/history")
    suspend fun getProductsHistory(
        @Header("Authorization") token: String,
    ): List<ProductDto>

    @GET("products/active")
    suspend fun getActiveProducts(
        @Header("Authorization") token: String,
    ): List<ProductDto>
}
