package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDao implements GenreStorage {
    JdbcTemplate jdbcTemplate;

    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenre() {
        encoding();
        String sqlQuret = "SELECT genre_id, genre_name FROM genres ORDER BY genre_id";
        return jdbcTemplate.query(sqlQuret, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreId(int id) throws NotFoundExeption {
        encoding();
        try {
            String sql = "SELECT * FROM genres WHERE genre_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } catch (Exception e) {
            throw new NotFoundExeption("Нет такого жанра");
        }
    }

    @Override
    public List<Genre> getByFilmId(int filmId) {
        encoding();
        String sqlQuery = "SELECT g.genre_id , " +
                "g.genre_name " +
                "FROM genres AS g " +
                "JOIN film_genres AS fg ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }

    public void encoding() {
        String sql = "MERGE INTO genres (genre_id, genre_name)\n" +
                "    VALUES (?, ?),\n" +
                "           (?, ?),\n" +
                "           (?, ?),\n" +
                "           (?, ?),\n" +
                "           (?, ?),\n" +
                "           (?, ?);";

        jdbcTemplate.update(sql, 1, "Комедия".getBytes(StandardCharsets.UTF_8),
                2, "Драма".getBytes(StandardCharsets.UTF_8),
                3, "Мультфильм".getBytes(StandardCharsets.UTF_8),
                4, "Триллер".getBytes(StandardCharsets.UTF_8),
                5, "Документальный".getBytes(StandardCharsets.UTF_8),
                6, "Боевик".getBytes(StandardCharsets.UTF_8));
    }
}
