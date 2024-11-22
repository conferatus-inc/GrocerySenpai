package org.example.mainbackend.dto

import org.example.mainbackend.model.User
import org.example.mainbackend.service.YandexIdService

data class UserLoginDTO(
    val responseYandexId: YandexIdService.ResponseYandexId,
    val user: User,
)
