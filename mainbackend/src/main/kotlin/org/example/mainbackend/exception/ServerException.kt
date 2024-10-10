package org.example.mainbackend.exception

import org.springframework.http.HttpStatus

data class ServerException(
    var status: HttpStatus,
    var code: String = status.name,
    override var message: String = status.name,
) : RuntimeException() {
    constructor(status: HttpStatus, code: String) : this(status, code, code)

    val answer: Map<String, Any>
        get() = java.util.Map.of<String, Any>("code", code, "message", message, "status", status.value())

    companion object {
        fun throwException(
            httpStatus: HttpStatus,
            message: String,
            moreInfo: String,
        ) {
            throw ServerException(httpStatus, message, moreInfo)
        }

        fun throwException(
            httpStatus: HttpStatus,
            message: String,
        ) {
            throw ServerException(httpStatus, message, message)
        }

        fun throwException(httpStatus: HttpStatus) {
            throw ServerException(httpStatus, httpStatus.name, "")
        }
    }
}
