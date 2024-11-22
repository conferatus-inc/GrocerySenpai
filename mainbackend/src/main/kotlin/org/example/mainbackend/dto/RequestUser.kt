package org.example.mainbackend.dto

import org.example.mainbackend.model.enums.RoleName

@JvmRecord
data class RequestUser(val username: String, val nickname: String, val roles: List<RoleName>)
