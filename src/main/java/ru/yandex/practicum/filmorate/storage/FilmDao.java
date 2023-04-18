package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface FilmDao {

    Film postFilm(Film film) throws ValidationException, NotFoundExeption;

    Film putFilm(Film film) throws ValidationException, NotFoundExeption;

    void deleteFilm(Integer id) throws ValidationException;

    Film getFilmId(Integer id) throws NotFoundExeption;

    List<Film> getAllFilm();

    Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException;
}
