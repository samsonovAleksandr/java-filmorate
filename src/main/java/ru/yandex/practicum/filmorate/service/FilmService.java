package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService implements FilmServiceInterface {
    private final JdbcTemplate jdbcTemplate;

    FilmDbStorage films;

    public FilmService(JdbcTemplate jdbcTemplate, FilmDbStorage films) {
        this.jdbcTemplate = jdbcTemplate;
        this.films = films;
    }

    @Override
    public void addLikeFilm(int idFilm, int idUser) throws NotFoundExeption {
        if (idFilm > 0 && idUser > 0) {
            String sql = "INSERT INTO film_likes (film_id, user_id)" +
                    "VALUES (?,?)";
            jdbcTemplate.update(sql, idFilm, idUser);
        } else {
            throw new NotFoundExeption("Нет такого фильма или юзера");
        }
    }

    @Override
    public void deleteLike(int idFilm, int idUser) throws NotFoundExeption {
        if (idFilm > 0 && idUser > 0) {
            String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
            jdbcTemplate.update(sql, idFilm, idUser);
        } else {
            throw new NotFoundExeption("Нет такого фильма или юзера");
        }
    }

    @Override
    public List<Film> topFilmLike(Integer length) {
        String sqlQuerte = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id, l.user_id " +
                "ORDER BY SUM(l.film_id) DESC, f.name " +
                "LIMIT(?)";

        return jdbcTemplate.query(sqlQuerte, films::mapRowToFilm, length);
    }
}
