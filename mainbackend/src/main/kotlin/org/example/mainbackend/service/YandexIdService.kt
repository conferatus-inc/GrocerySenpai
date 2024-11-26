package org.example.mainbackend.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import lombok.extern.slf4j.Slf4j
import org.example.mainbackend.exception.ServerExceptions
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException

@Service
@Slf4j
class YandexIdService {
    private val path = "https://login.yandex.ru/info"
    private val client = OkHttpClient()

    // TODO: настроить в зависимости от того, что будем требовать от пользователя при предоставлении токена
    @JvmRecord
    data class ResponseYandexId(
        val id: String,
        val login: String,
        val client_id: String,
        val display_name: String?,
        val real_name: String?,
        val first_name: String?,
        val last_name: String?,
        val sex: String?,
        val default_email: String?,
        val emails: List<String>?,
        val default_avatar_id: String?,
        val is_avatar_empty: Boolean?,
        val psuid: String,
    )

    fun getId(token: String): ResponseYandexId {
        val request =
            Request.Builder()
                .url(path)
                .addHeader("Authorization", "Bearer $token")
                .build()
        val call = client.newCall(request)
        log.info("request to yandex id with token {}...", token.substring(0, 20))
        var response: Response? = null
        try {
            response = call.execute()
            log.info(response.code().toString())
        } catch (e: IOException) {
            ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException()
        }
        try {
            if (response!!.code() != 200) {
                log.info("error with yandex token: {}", response.body().toString())
                ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException()
            }
            val responseString = response.body().string()
            log.info("Response string: {}", responseString)
            val responseYandexId =
                ObjectMapper().readValue(
                    responseString,
                    ResponseYandexId::class.java,
                )
            return responseYandexId
        } catch (e: IOException) {
            log.error(e.toString())
            log.error(e.message)
            ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException()
            throw RuntimeException()
        }
    }

    fun parseToken(authorization: String?): String {
        if (authorization == null || !authorization.contains("Bearer ")) {
            ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException()
        }
        return authorization!!.substring("Bearer ".length)
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
