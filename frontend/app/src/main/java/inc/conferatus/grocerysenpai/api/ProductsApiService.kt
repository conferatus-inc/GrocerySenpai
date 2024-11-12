package inc.conferatus.grocerysenpai.api

import inc.conferatus.grocerysenpai.presentation.mainlist.ProductDto
import inc.conferatus.grocerysenpai.presentation.mainlist.ProductsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductsApiService {
//    @POST("recommendations")
//    @Deprecated(message = "ne nado")
//    suspend fun postTask(@Body historyDto: SendHistoryDto): Int
//
//    @GET("result/{taskId}?")
//    @Deprecated(message = "ne nado")
//    suspend fun getTask(@Path("taskId") idx: Int, @Query("limit") limit: Int = 15): SuggestionsDto

    @GET("products/history")
    suspend fun getProductsHistory(
        @Header("Authorization") token: String,
    ): ProductsDto

    @GET("products/active")
    suspend fun getActiveProducts(
        @Header("Authorization") token: String,
    ): ProductsDto

    @POST("products")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body product: ProductDto,
    ): ProductDto

    @GET("products/delete/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
    ): ProductDto

    @POST("products/edit")
    suspend fun editProduct(
        @Header("Authorization") token: String,
        @Body product: ProductDto,
    ): ProductDto
}
