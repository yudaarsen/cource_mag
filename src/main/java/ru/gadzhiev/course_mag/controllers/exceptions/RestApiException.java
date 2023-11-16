package ru.gadzhiev.course_mag.controllers.exceptions;

import org.springframework.http.HttpStatus;

public class RestApiException extends Exception {

    protected final HttpStatus status;

    public RestApiException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
