package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

public Film postFilm(Film film) throws ValidationException;

public Film putFilm(Film film) throws ValidationException;

public void deleteFilm(Integer id) throws ValidationException;

public Film getFilmId(Integer id);

public List<Film> getAllFilm();
}
