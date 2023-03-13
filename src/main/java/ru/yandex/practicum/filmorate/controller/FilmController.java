package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.sarvice.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @Autowired
    FilmStorage films;

    @GetMapping("/films")
    public List<Film> getFilmList(){
        return films.getAllFilm();
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) throws ValidationException {
        return films.postFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) throws ValidationException {
        return films.putFilm(film);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable int id) throws ValidationException {
        films.deleteFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId){
        service.addLikeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId){
        service.deleteLike(id, userId);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> popularFilms(@RequestParam(required = false) int count){
       return service.topFilmLike(count);
    }

}
