package org.example.mainbackend.dto

@JvmRecord
data class RequestUser(val username: String, val nickname: String, val roles: List<String>)
