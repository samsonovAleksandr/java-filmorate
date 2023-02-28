package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private int id = 0;

    private final HashMap<Integer, Film> films = new HashMap<>();
    private static final LocalDate DATA_MIN = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public Map<Integer, Film> getFilms() {
        log.debug("Получили список фильмов: {}", films);
        return films;
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) throws ValidationException {
        films.put(id + 1, filmValidator(film));
        film.setId(newId());
        log.debug("Добавили новый фильм: {}", film);
        return film;
    }


    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            films.put(film.getId(), filmValidator(film));
            log.debug("Обновили пользователя с id: {}", film.getId());
        } else {
            log.warn("Нет фильма с таким ID: {}", film.getId());
            throw new ValidationException("С таким ID нет фильма!");
        }
        return film;
    }

    private boolean charDescriptFilm(String str) {
        char[] stringToArray = str.toCharArray();
        return stringToArray.length <= 200;
    }

    private Film filmValidator(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.warn("Ввведена пустая строка в названии фильма: {}", film.getName());
            throw new ValidationException("Название не может быть пустым!");
        }
        if (!charDescriptFilm(film.getDescription())) {
            log.warn("Описани длинее 200 символов: {}", film.getDescription());
            throw new ValidationException("Слишком длинное описание!");
        }
        if (film.getReleaseDate().isBefore(DATA_MIN)) {
            log.warn("Дата фильма слишком старая: {}", film.getReleaseDate());
            throw new ValidationException("Дата фильма до 28 декабря 1895");
        }
        if (film.getDuration() <= 0) {
            log.warn("Длина фильма отрицательная: {}", film.getDuration());
            throw new ValidationException("Длительность фильма должна быть положительная!");
        }
        return film;
    }

    private int newId() {
        return ++id;
    }
}
