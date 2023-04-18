package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@Slf4j
public class MpaController {

    private final FilmService filmService;

    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpa() {
        return filmService.getMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaId(@PathVariable int id) throws NotFoundExeption {
        return filmService.getMpaId(id);
    }
}
