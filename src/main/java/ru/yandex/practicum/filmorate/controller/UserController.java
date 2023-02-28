package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private int id = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Map<Integer, User> getUsers() {
        log.debug("Получили список Users: {}", users);
        return users;
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) throws ValidationException {
        users.put(id + 1, userValidator(user));
        user.setId(newId());
        log.debug("Добавили пользователя с ID: {}", user.getId());
        return user;
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), userValidator(user));
            log.debug("Обновили пользователя с id: {}", user.getId());
        } else {
            log.warn("Нет юзера с таким ID: {}", user.getId());
            throw new ValidationException("Юзера с таким ID нет!");
        }
        return user;
    }


    private User userValidator(User user) throws ValidationException {
        LocalDate nowDate = LocalDate.now();
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Направильно написан емайл или пустой: {}", user.getEmail());
            throw new ValidationException("Введен не емайл!");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Направильно написан логин или пустой: {}", user.getLogin());
            throw new ValidationException("Логин вводится без пробелов!");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("Имя было пустым, поэтому применили к полю name начение поля login");
        }

        if (user.getBirthday().isAfter(nowDate)) {
            log.warn("Дата рождения написана неверно: {}", user.getBirthday());
            throw new ValidationException("Дата рождения неможет быть в будущем!");
        }
        return user;
    }

    private int newId() {
        return ++id;
    }
}
