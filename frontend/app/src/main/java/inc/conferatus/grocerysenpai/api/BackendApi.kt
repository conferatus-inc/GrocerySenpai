package inc.conferatus.grocerysenpai.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.Instant

object BackendApi {
    private const val URL = "http://158.160.96.57:8080/api/v1/"
    private const val QR_URL = "http://158.160.96.57:8083/api/"

    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            var newRequest = originalRequest

            val response = chain.proceed(newRequest)
            if (response.code() != 200) {
                val a = 5
                println(originalRequest)
                println(response)
            }

            return response
        }
    }).build()


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .registerTypeAdapter(Instant::class.java, InstantJsonSerializer())
                        .registerTypeAdapter(Instant::class.java, InstantJsonDeserializer())
                        .create(),
                ),
            )
            .build()
    }

    private val retrofitQr: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(QR_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .registerTypeAdapter(Instant::class.java, InstantJsonSerializer())
                        .registerTypeAdapter(Instant::class.java, InstantJsonDeserializer())
                        .create(),
                ),
            )
            .build()
    }

    private class InstantJsonSerializer : JsonSerializer<Instant> {
        override fun serialize(
            src: Instant?,
            typeOfSrc: Type?,
            context: com.google.gson.JsonSerializationContext?
        ): com.google.gson.JsonElement {
            return JsonPrimitive(src.toString())
        }
    }

    private class InstantJsonDeserializer : JsonDeserializer<Instant> {
        override fun deserialize(
            json: com.google.gson.JsonElement?,
            typeOfT: Type?,
            context: com.google.gson.JsonDeserializationContext?
        ): Instant {
            return Instant.parse(json!!.asString)
        }
    }


    private val retrofitLogin: Retrofit by lazy {
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
        retrofitQr.create(QrApiService::class.java)
    }

    val changesApi: ChangesApiService by lazy {
        retrofit.create(ChangesApiService::class.java)
    }

    val loginApi: LoginApiService by lazy {
        retrofitLogin.create(LoginApiService::class.java)
    }
}
