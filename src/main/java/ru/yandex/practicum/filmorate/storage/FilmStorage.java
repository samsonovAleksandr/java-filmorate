package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

interface FilmStorage {

    Film postFilm(Film film) throws ValidationException;

    Film putFilm(Film film) throws ValidationException, NotFoundExeption;

    void deleteFilm(Integer id) throws ValidationException;

    Film getFilmId(Integer id) throws NotFoundExeption;

    List<Film> getAllFilm();
}
