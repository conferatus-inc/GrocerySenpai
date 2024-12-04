package inc.conferatus.grocerysenpai.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.Instant

interface ChangesApiService {
    @GET("/api/v1/changes/")
    suspend fun getChanges(
        @Header("Authorization") token: String,
        @Query("fromTime") fromTime: Instant,
    ): List<ChangeDto>

    @POST("/api/v1/changes/")
    suspend fun makeChanges(
        @Header("Authorization") token: String,
        @Body changes: List<ChangeDto>,
    ): List<ProductDto>
}
