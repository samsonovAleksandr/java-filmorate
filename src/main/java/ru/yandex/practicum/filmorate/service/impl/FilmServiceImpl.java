package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmDao;
import ru.yandex.practicum.filmorate.storage.GenreDao;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.util.List;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final JdbcTemplate jdbcTemplate;

    private final FilmDao films;

    private final GenreDao genreDao;

    private final MpaDao mpaDao;

    public FilmServiceImpl(JdbcTemplate jdbcTemplate, FilmDao films, GenreDao genreDao, MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.films = films;
        this.genreDao = genreDao;
        this.mpaDao = mpaDao;
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

    @Override
    public Film postFilm(Film film) throws ValidationException, NotFoundExeption {
        return films.postFilm(film);
    }

    @Override
    public Film putFilm(Film film) throws ValidationException, NotFoundExeption {
        return films.putFilm(film);
    }

    @Override
    public void deleteFilm(Integer id) throws ValidationException {
        films.deleteFilm(id);
    }

    @Override
    public Film getFilmId(Integer id) throws NotFoundExeption {
        return films.getFilmId(id);
    }

    @Override
    public List<Film> getAllFilm() {
        return films.getAllFilm();
    }

    @Override
    public List<Genre> getGenre() {
        return genreDao.getGenre();
    }

    @Override
    public Genre getGenreId(int id) throws NotFoundExeption {
        return genreDao.getGenreId(id);
    }

    @Override
    public List<Genre> getByFilmId(int filmId) {
        return genreDao.getByFilmId(filmId);
    }

    @Override
    public List<Mpa> getMpa() {
        return mpaDao.getMpa();
    }

    @Override
    public Mpa getMpaId(int id) throws NotFoundExeption {
        return mpaDao.getMpaId(id);
    }

}
