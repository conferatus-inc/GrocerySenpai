package org.example.mainbackend.dto;


import org.example.mainbackend.model.User;
import org.example.mainbackend.service.YandexIdService;

public record UserLoginDTO(YandexIdService.ResponseYandexId responseYandexId,
                           User appUser) {
}
