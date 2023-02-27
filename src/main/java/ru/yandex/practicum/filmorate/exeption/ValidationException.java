package ru.yandex.practicum.filmorate.exeption;

public class ValidationException extends Throwable{
    public ValidationException(String message) {
        super(message);
    }
}
