package org.example.mainbackend.exception

import org.springframework.http.HttpStatus

enum class ServerExceptions {
    NOT_FOUND(HttpStatus.NOT_FOUND),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED),
    ILLEGAL_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED),
    ILLEGAL_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED),
    BAD_REFRESH_TOKEN(HttpStatus.BAD_REQUEST),
    NOT_CURRENT_REFRESH_TOKEN(HttpStatus.FORBIDDEN),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    NO_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_PROBLEM(HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTS(HttpStatus.BAD_REQUEST),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    BAD_LOGIN(HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    ABOBA(HttpStatus.I_AM_A_TEAPOT),
    ILLEGAL_YANDEX_TOKEN(HttpStatus.BAD_REQUEST),
    ;

    private val serverException: ServerException

    constructor(httpStatus: HttpStatus, message: String, moreInfo: String) {
        serverException = ServerException(httpStatus, message, moreInfo)
    }

    constructor(httpStatus: HttpStatus, message: String) {
        serverException = ServerException(httpStatus, message)
    }

    constructor(httpStatus: HttpStatus) {
        serverException = ServerException(httpStatus, name)
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
