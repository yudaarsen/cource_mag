package ru.gadzhiev.course_mag.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 * Base response model for REST API
 * @author Arseniy Gadzhiev
 */
public class Response {

    @JsonIgnore
    private final HttpStatus status;
    private final String message;

    public Response() {
        status = null;
        message = "";
    }

    public Response(final HttpStatus status) {
        this.status = status;
        this.message = "";
    }

    public Response(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty(value = "status", index = 1)
    public int getHttpCode() {
        return status != null ? status.value() : 0;
    }
}
