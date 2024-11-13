package inc.conferatus.grocerysenpai.api

import retrofit2.http.GET
import retrofit2.http.Path

interface QrApiService {
    @GET("qr_code/{raw_qr}")
    @Deprecated(message = "not implemented yet")
    suspend fun getQrData(
        @Path("raw_qr") qrData: String,
    ): QrProductsDto
}
