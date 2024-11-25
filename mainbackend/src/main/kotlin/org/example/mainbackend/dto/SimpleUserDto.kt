package org.example.mainbackend.dto

import org.example.mainbackend.model.User

data class SimpleUserDto(
    val id: Long,
    val username: String,
) {
    constructor(user: User) : this(
        user.id!!,
        user.username,
    )
}
