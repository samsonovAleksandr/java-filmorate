package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User postUser(User user) throws ValidationException;

    User putUser(User user) throws ValidationException, NotFoundExeption;

    void deleteUser(Integer id) throws ValidationException;

    User getUserId(Integer id) throws NotFoundExeption;

    List<User> getAllUser();
}
