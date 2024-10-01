package org.conferatus.timetable.backend.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public enum ServerExceptions {
    NOT_FOUND_EXCEPTION(new ServerException(HttpStatus.NOT_FOUND, "NOT_FOUND_EXCEPTION")),
    ACCESS_TOKEN_EXPIRED(new ServerException(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED")),
    REFRESH_TOKEN_EXPIRED(new ServerException(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED")),
    ILLEGAL_ACCESS_TOKEN(new ServerException(HttpStatus.UNAUTHORIZED, "ILLEGAL_ACCESS_TOKEN")),
    ILLEGAL_REFRESH_TOKEN(new ServerException(HttpStatus.UNAUTHORIZED, "ILLEGAL_REFRESH_TOKEN")),
    UNAUTHORIZED(new ServerException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED")),
    INVALID_PARAMETER(new ServerException(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER")),
    BAD_REQUEST(new ServerException(HttpStatus.BAD_REQUEST)),
    NO_ACCESS_TOKEN(new ServerException(HttpStatus.UNAUTHORIZED, "NO_ACCESS_TOKEN")),
    ACCESS_TOKEN_PROBLEM(new ServerException(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_PROBLEM")),

    ;

    private final ServerException serverException;

    ServerExceptions(HttpStatus httpStatus, String message, String moreInfo) {
        serverException = new ServerException(httpStatus, message, moreInfo);
    }

    ServerExceptions(ServerException serverException) {
        this.serverException = serverException;
    }

    public ServerExceptions message(String message) {
        this.serverException.setCode(message);
        return this;
    }

    public ServerExceptions moreInfo(String moreInfo) {
        this.serverException.setMessage(moreInfo);
        return this;
    }

    public void throwException() {
        throw serverException;
    }

    public ServerException getServerException() {
        return serverException;
    }

    public Map<String, Object> getAnswer() {
        return serverException.getAnswer();
    }

    public int status() {
        return serverException.status.value();
    }
}
