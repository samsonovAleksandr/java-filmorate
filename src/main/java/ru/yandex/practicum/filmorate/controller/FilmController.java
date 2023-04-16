package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final FilmService service;

    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping("/films")
    public List<Film> getFilmList() {
        return service.getAllFilm();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) throws NotFoundExeption {
        return service.getFilmId(id);
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) throws ValidationException, NotFoundExeption {
        return service.postFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) throws ValidationException, NotFoundExeption {
        return service.putFilm(film);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable int id) throws ValidationException {
        service.deleteFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) throws NotFoundExeption {
        service.addLikeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) throws NotFoundExeption {
        service.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> popularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return service.topFilmLike(count);
    }

}
