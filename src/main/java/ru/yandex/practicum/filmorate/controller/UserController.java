package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return service.getAllUser();
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) throws ValidationException {
        return service.postUser(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws NotFoundExeption, ValidationException {
        return service.putUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) throws ValidationException {
        service.deleteUser(id);
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable int id) throws NotFoundExeption {
        return service.getUserId(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public boolean putFriend(@PathVariable int id, @PathVariable int friendId) throws NotFoundExeption {
        return service.putFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriendUser(@PathVariable int id, @PathVariable int friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findListUserFriends(@PathVariable int id) throws NotFoundExeption {
        return service.listFriendUserId(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findGeneralFriends(@PathVariable int id, @PathVariable int otherId) {
        return service.listOfMutualFriends(id, otherId);
    }
}

