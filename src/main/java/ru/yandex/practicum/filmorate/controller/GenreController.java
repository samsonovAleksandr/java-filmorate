package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;

@RestController
@Slf4j
public class GenreController {
    private GenreDao genreDao;

    public GenreController(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @GetMapping("/genres")
    public List<Genre> getGenre() {
        return genreDao.getGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreId(@PathVariable int id) throws NotFoundExeption {
        return genreDao.getGenreId(id);
    }
}
