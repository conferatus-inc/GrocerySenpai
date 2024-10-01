package org.conferatus.timetable.backend.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class ServerException extends RuntimeException {
    HttpStatus status;
    String code;
    String message;

    public ServerException(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ServerException(HttpStatus status, String code) {
        this(status, code, code);
    }

    public ServerException(HttpStatus status) {
        this(status, status.name(), status.name());
    }

    public static void throwException(HttpStatus httpStatus, String message, String moreInfo) {
        throw new ServerException(httpStatus, message, moreInfo);
    }

    public static void throwException(HttpStatus httpStatus, String message) {
        throw new ServerException(httpStatus, message, message);
    }

    public static void throwException(HttpStatus httpStatus) {
        throw new ServerException(httpStatus, httpStatus.name(), "");
    }

    public Map<String, Object> getAnswer() {
        return Map.of("code", code, "message", message, "status", status.value());
    }
}
