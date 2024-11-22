package org.example.mainbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.mainbackend.exception.ServerExceptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class YandexIdService {
    private final String path = "https://login.yandex.ru/info";
    private final OkHttpClient client = new OkHttpClient();

    //TODO: настроить в зависимости от того, что будем требовать от пользователя при предоставлении токена
    public static record ResponseYandexId(String id,
                                          String login,
                                          String client_id,
                                          String display_name,
                                          String real_name,
                                          String first_name,
                                          String last_name,
                                          String sex,
                                          String default_email,
                                          List<String> emails,
                                          String default_avatar_id,
                                          Boolean is_avatar_empty,
                                          String psuid
    ) {

    }


    public ResponseYandexId getId(String token) {
        Request request = new Request.Builder()
                .url(path)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        log.info("request to yandex id with token {}...", token.substring(0, 20));
        Response response = null;
        try {
            response = call.execute();
            log.info(String.valueOf(response.code()));
        } catch (IOException e) {
            ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException();
        }
        try {
            if (response.code() != 200) {
                log.info("error with yandex token: {}", response.body().toString());
                ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException();
            }
            var responseString = response.body().string();
            log.info("Response string: {}", responseString);
            ResponseYandexId responseYandexId = new ObjectMapper().readValue(responseString,
                    ResponseYandexId.class);
            return responseYandexId;
        } catch (IOException e) {
            log.error(e.toString());
            log.error(e.getMessage());
            ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException();
        }

        return null;
    }

    public String parseToken(String authorization) {
        if (authorization == null || !authorization.contains("Bearer ")) {
            ServerExceptions.ILLEGAL_YANDEX_TOKEN.throwException();
        }
        return authorization.substring("Bearer ".length());
    }
}
