package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {


    @Autowired
    private final FilmService filmService;
    private final FilmDbStorage filmDbStorage;

    private final UserDbStorage userDbStorage;


    @Test
    void postUser() throws ValidationException, NotFoundExeption {
        Film film = Film.builder()
                .name("Drive")
                .description("Ryan Gosling starring.")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .build();
        filmDbStorage.postFilm(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
    }

    @Test
    void putFilm() throws ValidationException, NotFoundExeption {
        Film film = Film.builder()
                .name("Drive")
                .description("Ryan Gosling starring.")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .build();
        filmDbStorage.postFilm(film);
        Film film1 = Film.builder()
                .id(1)
                .name("DriveUP")
                .description("Ryan Gosling starring.UP")
                .duration(140)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .build();
        filmDbStorage.putFilm(film1);
        AssertionsForClassTypes.assertThat(filmDbStorage.getFilmId(film1.getId()))
                .hasFieldOrPropertyWithValue("id", film1.getId());
    }

    @Test
    void findFilmById() throws ValidationException, NotFoundExeption {
        Film film = Film.builder()
                .name("Drive")
                .description("Ryan Gosling starring.")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .build();
        filmDbStorage.postFilm(film);
        AssertionsForClassTypes.assertThat(filmDbStorage.getFilmId(film.getId()))
                .hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void notFoundFilm() {
        Film film = Film.builder()
                .id(9999)
                .name("Drive")
                .description("Ryan Gosling starring.")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .build();
        Assertions.assertThatThrownBy(() -> filmDbStorage.putFilm(film));
    }

    @Test
    void getAllFilms() throws ValidationException, NotFoundExeption {
        Film film = Film.builder()
                .name("Drive")
                .description("Ryan Gosling starring.")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .build();
        filmDbStorage.postFilm(film);
        Collection<Film> films = filmDbStorage.getAllFilm();
        Assertions.assertThat(films).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(films).extracting("name").contains(film.getName());
        Assertions.assertThat(films).extracting("description").contains(film.getDescription());
    }

    @Test
    void addLikeFilm() throws ValidationException, NotFoundExeption {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();

        Film film = Film.builder()
                .name("Drive")
                .description("Ryan Gosling starring.")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .genres(null)
                .build();

        userDbStorage.postUser(user);
        filmDbStorage.postFilm(film);
        filmService.addLikeFilm(film.getId(), user.getId());
        Assertions.assertThat(filmService.topFilmLike(1).size() == 1);
    }

    @Test
    void getBestFilmList() throws ValidationException, NotFoundExeption {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();

        User user2 = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();

        Film film = Film.builder()
                .name("Drive")
                .description("Ryan Gosling starring.")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .genres(null)
                .build();

        Film film2 = Film.builder()
                .name("Drive2")
                .description("Ryan Gosling starring.2")
                .duration(135)
                .releaseDate(LocalDate.of(2011, 7, 2))
                .mpa(Mpa.builder()
                        .id(1)
                        .name("PG")
                        .build())
                .genres(null)
                .build();

        userDbStorage.postUser(user);
        userDbStorage.postUser(user2);
        filmDbStorage.postFilm(film);
        filmDbStorage.postFilm(film2);
        filmService.addLikeFilm(film.getId(), user.getId());
        filmService.addLikeFilm(film.getId(), user2.getId());
        filmService.addLikeFilm(film2.getId(), user.getId());
        Assertions.assertThat(filmService.topFilmLike(2).get(1).getName().equals(film.getName()));

    }

}