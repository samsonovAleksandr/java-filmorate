package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmGenreDaoImpl {

    JdbcTemplate jdbcTemplate;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFilmGenre(Film film) {
        deleteFilmGenre(film);
        List<Genre> genreList = film.getGenres();
        for (Genre gn : genreList) {
            String sql = "MERGE INTO film_genres KEY (genre_id, film_id)  VALUES (?, ?)";
            jdbcTemplate.update(sql,
                    gn.getId(),
                    film.getId());
        }
    }

    public void deleteFilmGenre(Film film) {
        if (film.getGenres() != null) {
            String sql = "DELETE film_genres WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getId());
        }
    }

    public List<FilmGenre> getFilm(Film film) {
        String sql = "SELECT * FROM film_genres WHERE film_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToFilmGenre, film.getId());
    }

    public List<FilmGenre> getFilmGenre() {
        String sql = "SELECT * FROM film_genres";
        return jdbcTemplate.query(sql, this::mapRowToFilmGenre);
    }

    public FilmGenre mapRowToFilmGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .filmId(resultSet.getInt("film_id"))
                .genreId(resultSet.getInt("genre_id"))
                .build();
    }
}
