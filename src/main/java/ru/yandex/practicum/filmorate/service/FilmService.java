package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmService {
    void addLikeFilm(int idFilm, int idUser) throws NotFoundExeption;

    void deleteLike(int idFilm, int idUser) throws NotFoundExeption;

    List<Film> topFilmLike(Integer length);

    Film postFilm(Film film) throws ValidationException, NotFoundExeption;

    Film putFilm(Film film) throws ValidationException, NotFoundExeption;

    void deleteFilm(Integer id) throws ValidationException;

    Film getFilmId(Integer id) throws NotFoundExeption;

    List<Film> getAllFilm();

    List<Genre> getGenre();

    Genre getGenreId(int id) throws NotFoundExeption;

    List<Genre> getByFilmId(int filmId);

    List<Mpa> getMpa();

    Mpa getMpaId(int id) throws NotFoundExeption;
}
