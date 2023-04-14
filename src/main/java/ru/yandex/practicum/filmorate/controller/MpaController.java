package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.List;

@RestController
@Slf4j
public class MpaController {

    private MpaDao mpaDao;

    public MpaController(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpa() {
        return mpaDao.getMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaId(@PathVariable int id) throws NotFoundExeption {
        return mpaDao.getMpaId(id);
    }
}
