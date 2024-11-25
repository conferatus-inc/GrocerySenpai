package org.example.mainbackend.controller

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.example.mainbackend.exception.ServerException
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

@ControllerAdvice
class ExceptionHandlingController {
    @ExceptionHandler(ServerException::class)
    private fun handle(
        response: HttpServletResponse,
        serverException: ServerException,
    ) {
        response.status = serverException.status.value()
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        val mapper = ObjectMapper()
        try {
            BufferedWriter(OutputStreamWriter(response.outputStream)).use { bw ->
                bw.write(mapper.writeValueAsString(serverException.answer))
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
