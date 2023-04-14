package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmServiceInterface {

    void addLikeFilm(int idFilm, int idUser) throws NotFoundExeption;

    void deleteLike(int idFilm, int idUser) throws NotFoundExeption;

    List<Film> topFilmLike(Integer length);
}
