package ru.yandex.practicum.filmorate.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler(NotFoundExeption.class)
    public ResponseEntity<NotFoundExeption> notFoundException(NotFoundExeption exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new NotFoundExeption(exception.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationException> validationException(ValidationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationException(exception.getMessage()));
    }
}
