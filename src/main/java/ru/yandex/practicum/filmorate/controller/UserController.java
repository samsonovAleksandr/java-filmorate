package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sarvice.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Autowired
    InMemoryUserStorage users;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users.getAllUser();
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) throws ValidationException {
        return users.postUser(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws NotFoundExeption, ValidationException {
        return users.putUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) throws ValidationException {
        users.deleteUser(id);
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable int id) throws NotFoundExeption {
        return users.getUserId(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void putFriendUser(@PathVariable int id, @PathVariable int friendId) throws NotFoundExeption {
        service.putFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriendUser(@PathVariable int id, @PathVariable int friendId) throws NotFoundExeption {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findListUserFriends(@PathVariable int id) throws NotFoundExeption {
        return service.listFriendUserId(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findGeneralFriends(@PathVariable int id, @PathVariable int otherId) throws NotFoundExeption {
        return service.listOfMutualFriends(id, otherId);
    }
}

