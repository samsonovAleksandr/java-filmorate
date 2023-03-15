package ru.yandex.practicum.filmorate.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private FilmController filmController;

    @Test
    public void createAndGetAllFilms() throws Exception, ValidationException {
        Film film = new Film("Driver", "Starring Ryan Gosling"
                , LocalDate.of(2011, 10, 10), 100);
        film.setId(1);
        Film film1 = filmController.postFilm(film);
        Assert.assertEquals(film, film1);
        Assert.assertEquals(filmController.getFilmList().size(), 2);
    }

    @Test
    public void createFilmIfNameIsEmpty() throws ValidationException {
        Film film = new Film("", "Starring Ryan Gosling"
                , LocalDate.of(2011, 10, 10), 100);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
    }

    @Test
    public void createFilmIfDescriptionMore200Char() throws ValidationException {
        Film film = new Film("Driver", "Starring Ryan GoslingStarring Ryan GoslingStarring Ryan Gosling" +
                "Starring Ryan GoslingStarring Ryan GoslingStarring Ryan GoslingStarring Ryan GoslingStarring Ryan Gosling" +
                "Starring Ryan GoslingStarring Ryan GoslingStarring Ryan GoslingStarring Ryan GoslingStarring Ryan Gosling"
                , LocalDate.of(2011, 10, 10), 100);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
    }

    @Test
    public void releaseDataBeforeFirstMovieInWorld() {
        Film film = new Film("Driver", "Starring Ryan Gosling"
                , LocalDate.of(1855, 10, 10), 100);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
    }

    @Test
    public void durationFilmLessThanZeroIfZero() {
        Film film = new Film("Driver", "Starring Ryan Gosling"
                , LocalDate.of(2011, 10, 10), -1);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film);
        });
        Film film1 = new Film("Driver", "Starring Ryan Gosling"
                , LocalDate.of(2011, 10, 10), 0);
        Assertions.assertThrows(ValidationException.class, () -> {
            filmController.postFilm(film1);
        });
    }

    @Test
    public void putFilm() throws ValidationException, NotFoundExeption {
        Film film = new Film("Driver", "Starring Ryan Gosling"
                , LocalDate.of(2011, 10, 10), 100);
        filmController.postFilm(film);
        Film film1 = new Film("Driver", "Starring Ryan Gosling"
                , LocalDate.of(2011, 10, 10), 96);
        film1.setId(1);
        filmController.putFilm(film1);
        Assertions.assertEquals(filmController.getFilmList().get(film1.getId()).getDuration(), 96);
    }

    @Test
    public void createFilmNull() {
        Film film = new Film(null, null
                , null, 0);
        Assertions.assertThrows(NullPointerException.class, () -> {
            filmController.postFilm(film);
        });
    }
}
