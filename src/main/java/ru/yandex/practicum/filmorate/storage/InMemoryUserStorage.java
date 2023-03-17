package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private int id = 0;
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public User postUser(User user) throws ValidationException {
        users.put(id + 1, userValidator(user));
        user.setId(newId());
        log.debug("Добавили пользователя с ID: {}", user.getId());
        return user;
    }

    @Override
    public User putUser(User user) throws ValidationException, NotFoundExeption {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            users.put(user.getId(), userValidator(user));
            log.debug("Обновили пользователя с id: {}", user.getId());
        } else {
            log.warn("Нет юзера с таким ID: {}", user.getId());
            throw new NotFoundExeption("Юзера с таким ID : " + user.getId() + " нет!");
        }
        return user;
    }

    @Override
    public void deleteUser(Integer id) throws ValidationException {
        if (users.containsKey(id)){
            log.debug("Удалили пользователя с ID: {}", id);
            users.keySet().removeIf(key -> key.equals(id));
        } else {
            log.warn("Нет пользователя с таким ID: {}", id);
            throw new ValidationException("Юзера с таким ID : " + id + " нет!");
        }
    }

    @Override
    public User getUserId(Integer id) throws NotFoundExeption {
        if (users.get(id) != null){
            log.debug("Выгрузили пользователя с ID: {}", id);
            return users.get(id);
        } else{
            throw new NotFoundExeption("Юзера с таким ID : " + id + " нет!");
        }

    }

    @Override
    public List<User> getAllUser() {
        log.debug("Получили список Users: {}", users);
        return new ArrayList<>(users.values());
    }

    private User userValidator(User user) throws ValidationException {
        LocalDate nowDate = LocalDate.now();
        if (user.getBirthday().isAfter(nowDate)) {
            log.warn("Дата рождения написана неверно: {}", user.getBirthday());
            throw new ValidationException("Дата рождения неможет быть в будущем!");
        }

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Неправильно написан емайл или пустой: {}", user.getEmail());
            throw new ValidationException("Введен не емайл!");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Неправильно написан логин или пустой: {}", user.getLogin());
            throw new ValidationException("Логин вводится без пробелов!");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("Имя было пустым, поэтому применили к полю name начение поля login");
        }
        return user;
    }

    private int newId() {
        return ++id;
    }
}
