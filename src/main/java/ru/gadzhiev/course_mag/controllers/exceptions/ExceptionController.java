package ru.gadzhiev.course_mag.controllers.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.gadzhiev.course_mag.models.responses.Response;

import java.util.Arrays;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class, ConstraintViolationException.class })
    @ResponseBody
    public Response generalRequestExceptionHandling(Exception exception) {
        return new Response(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseBody
    public Response methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        return new Response(HttpStatus.BAD_REQUEST, Arrays.toString(exception.getDetailMessageArguments()));
    }

}
