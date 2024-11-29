package inc.conferatus.grocerysenpai.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiService {
    @GET("accounts/login")
    suspend fun login(
        @Header("Authorization") yandexToken: String,
        @Header("role") role: Role,
    ): Map<String, Any>

    @POST("accounts/token/refresh")
    suspend fun refresh(
        @Header("Authorization") token: String,
    ): Response<Map<String, Any>>

    @GET("accounts/token/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
    ): Map<String, Any>
}
