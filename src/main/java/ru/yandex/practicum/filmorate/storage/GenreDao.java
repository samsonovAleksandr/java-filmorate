package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> getGenre();

    Genre getGenreId(int id) throws NotFoundExeption;

    List<Genre> getByFilmId(int filmId);
}
