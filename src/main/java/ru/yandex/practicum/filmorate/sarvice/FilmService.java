package ru.yandex.practicum.filmorate.sarvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    @Autowired
    InMemoryFilmStorage films;

    public void addLikeFilm(int idFilm, int idUser) throws NotFoundExeption {
        if (!films.getFilmId(idFilm).getLikeUser().contains(idUser)) {
            films.getFilmId(idFilm).addListLikeUser(idUser);
            log.debug("Пользователь {} поставил лайк фильму {}", idUser, idFilm);
        } else {
            throw new NotFoundExeption("ЛАйк уже поставлен");
        }
    }

    public void deleteLike(int idFilm, int idUser) throws NotFoundExeption {
        if (films.getFilmId(idFilm).getLikeUser().contains(idUser)) {
            films.getFilmId(idFilm).removeLike(idUser);
            log.debug("Пользователь {} удалил лайк фильму {}", idUser, idFilm);
        } else {
            throw new NotFoundExeption("Нет лайка от этого юзера");
        }
    }

    public List<Film> topFilmLike(Integer length) {
        List<Film> filmsList = films.getAllFilm();
        filmsList.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return Integer.compare(o2.getLikeUser().size(), o1.getLikeUser().size());
            }
        });

        if (filmsList.size() >= length) {
            log.debug("Вывели спиок фильмов по количеству лайков длиной: {}", length);
            return filmsList.subList(0, length);
        }
        log.debug("Вывели спиок фильмов по количеству лайков длиной: {}", filmsList.size());
        return filmsList;
    }
}
