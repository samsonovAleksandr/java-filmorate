package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sarvice.UserService;

import java.util.List;

public interface UserStorage {
    public User postUser(User user) throws ValidationException;

    public User putUser(User user) throws ValidationException;

    public void deleteUser(Integer id) throws ValidationException;

    public User getUserId(Integer id);

    public List<User> getAllUser();
}
