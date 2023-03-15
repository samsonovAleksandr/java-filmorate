package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User postUser(User user) throws ValidationException;

    public User putUser(User user) throws ValidationException, NotFoundExeption;

    public void deleteUser(Integer id) throws ValidationException;

    public User getUserId(Integer id) throws NotFoundExeption;

    public List<User> getAllUser();
}
