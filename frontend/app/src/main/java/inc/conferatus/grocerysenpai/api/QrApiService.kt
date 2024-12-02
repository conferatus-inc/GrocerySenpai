package inc.conferatus.grocerysenpai.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface QrApiService {
    @GET("qr_code/{raw_qr}")
    suspend fun getQrData(
        @Path("raw_qr") qrData: String,
    ): QrProductsDto
}
