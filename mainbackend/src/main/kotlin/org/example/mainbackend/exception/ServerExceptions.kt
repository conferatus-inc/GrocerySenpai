package org.example.mainbackend.exception

import org.springframework.http.HttpStatus

enum class ServerExceptions {
    NOT_FOUND_EXCEPTION(ServerException(HttpStatus.NOT_FOUND, "NOT_FOUND_EXCEPTION")),
    ACCESS_TOKEN_EXPIRED(ServerException(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED")),
    REFRESH_TOKEN_EXPIRED(ServerException(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED")),
    ILLEGAL_ACCESS_TOKEN(ServerException(HttpStatus.UNAUTHORIZED, "ILLEGAL_ACCESS_TOKEN")),
    ILLEGAL_REFRESH_TOKEN(ServerException(HttpStatus.UNAUTHORIZED, "ILLEGAL_REFRESH_TOKEN")),
    UNAUTHORIZED(ServerException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED")),
    INVALID_PARAMETER(ServerException(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER")),
    BAD_REQUEST(ServerException(HttpStatus.BAD_REQUEST)),
    NO_ACCESS_TOKEN(ServerException(HttpStatus.UNAUTHORIZED, "NO_ACCESS_TOKEN")),
    ACCESS_TOKEN_PROBLEM(ServerException(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_PROBLEM")),
    USER_ALREADY_EXISTS(ServerException(HttpStatus.BAD_REQUEST, "USER_ALREADY_EXISTS")),
    ROLE_NOT_EXISTS(ServerException(HttpStatus.BAD_REQUEST, "ROLE_NOT_EXISTS")),
    FORBIDDEN(ServerException(HttpStatus.FORBIDDEN, "FORBIDDEN")),
    ;

    private val serverException: ServerException

    constructor(httpStatus: HttpStatus, message: String, moreInfo: String) {
        serverException = ServerException(httpStatus, message, moreInfo)
    }

    constructor(serverException: ServerException) {
        this.serverException = serverException
    }

    fun message(message: String): ServerExceptions {
        serverException.code = message
        return this
    }

    fun moreInfo(moreInfo: String): ServerExceptions {
        serverException.message = moreInfo
        return this
    }

    fun throwException() {
        throw serverException
    }

    val answer: Map<String, Any>
        get() = serverException.answer

    fun status(): Int {
        return serverException.status.value()
    }
}
