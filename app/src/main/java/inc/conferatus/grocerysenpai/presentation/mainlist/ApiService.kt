package inc.conferatus.grocerysenpai.presentation.mainlist

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.Flow

interface ApiService {
    @POST("recommendations")
    suspend fun postTask(@Body historyDto: SendHistoryDto): Int

    @GET("result/{taskId}?")
    suspend fun getTask(@Path("taskId") idx: Int, @Query("limit") limit: Int = 15): SuggestionsDto
}
