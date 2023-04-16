package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> getMpa();

    Mpa getMpaId(int id) throws NotFoundExeption;

}
