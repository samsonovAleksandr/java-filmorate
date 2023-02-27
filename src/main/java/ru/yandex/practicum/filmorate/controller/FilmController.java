package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> getFilms(){
        return films;
    }

    @PostMapping("/film")
    public Film postFilm(@RequestBody Film film){
        films.add(film);
        return film;
    }


    @PutMapping
    public Film putFilm(@RequestBody Film film){
        
    }
}
