package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    boolean putFriend(int idUser, int idFriend) throws NotFoundExeption;

    void deleteFriend(int idUser, int idFriend);

    List<User> listOfMutualFriends(int idUser1, int idUser2);

    List<User> listFriendUserId(int id) throws NotFoundExeption;

    boolean containsUser(int id) throws NotFoundExeption;

    boolean getFriendStatus(int id, int friendId);

    User postUser(User user) throws ValidationException;

    User putUser(User user) throws ValidationException, NotFoundExeption;

    void deleteUser(Integer id) throws ValidationException;

    User getUserId(Integer id) throws NotFoundExeption;

    List<User> getAllUser();
}
