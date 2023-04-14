package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private static final LocalDate DATA_MIN = LocalDate.of(1895, 12, 28);

    private final JdbcTemplate jdbcTemplate;

    private final FilmGenreDao filmGenreDao;

    private final GenreDao genreDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenreDao filmGenreDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDao = filmGenreDao;
        this.genreDao = genreDao;
    }

    @Override
    public Film postFilm(Film film) throws ValidationException, NotFoundExeption {
        filmValidator(film);
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                " VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        film.setId(id);

        if (film.getGenres() != null){
            filmGenreDao.addFilmGenre(film);
        }

        return getFilmId(id);
    }

    @Override
    public Film putFilm(Film film) throws ValidationException, NotFoundExeption {
        filmValidator(film);
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            filmGenreDao.addFilmGenre(film);
        }
        return getFilmId(film.getId());
    }

    @Override
    public void deleteFilm(Integer id) {
        String sqlQuery = "DETELE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film getFilmId(Integer id) throws NotFoundExeption {
        try {
            String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name," +
                    " FROM films AS f" +
                    " LEFT JOIN mpa AS m ON f.mpa_id=m.mpa_id " +
                    " WHERE f.film_id = ?" +
                    "ORDER BY f.film_id";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (Exception e) {
            throw new NotFoundExeption("Нет такого фильма");
        }
    }

    @Override
    public List<Film> getAllFilm() {
        String sqlQuery = "SELECT f.*, m.mpa_name" +
                " FROM films AS f" +
                " JOIN mpa AS m ON f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private boolean charDescriptFilm(String str) {
        char[] stringToArray = str.toCharArray();
        return stringToArray.length <= 200;
    }

    private Film filmValidator(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.warn("Ввведена пустая строка в названии фильма: {}", film.getName());
            throw new ValidationException("Название не может быть пустым!");
        }
        if (!charDescriptFilm(film.getDescription())) {
            log.warn("Описани длинее 200 символов: {}", film.getDescription());
            throw new ValidationException("Слишком длинное описание!");
        }
        if (film.getReleaseDate().isBefore(DATA_MIN)) {
            log.warn("Дата фильма слишком старая: {}", film.getReleaseDate());
            throw new ValidationException("Дата фильма до 28 декабря 1895");
        }
        if (film.getDuration() <= 0) {
            log.warn("Длина фильма отрицательная: {}", film.getDuration());
            throw new ValidationException("Длительность фильма должна быть положительная!");
        }
        return film;
    }

    public Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film.FilmBuilder builder = Film.builder();
        builder.id(resultSet.getInt("film_id"));
        builder.name(resultSet.getString("name"));
        builder.description(resultSet.getString("description"));
        builder.releaseDate(resultSet.getDate("release_date").toLocalDate());
        builder.duration(resultSet.getInt("duration"));
        builder.mpa(Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build());
        builder.genres(genreDao.getByFilmId(resultSet.getInt("film_id")));
        return builder
                .build();
    }
}