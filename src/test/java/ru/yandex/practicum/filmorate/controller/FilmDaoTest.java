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
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.dao.FilmDaoImpl;
import ru.yandex.practicum.filmorate.storage.dao.UserDaoImpl;

import java.time.LocalDate;
import java.util.Collection;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {


    @Autowired
    private final FilmServiceImpl filmServiceImpl;
    private final FilmDaoImpl filmDaoImpl;

    private final UserDaoImpl userDaoImpl;


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
        filmDaoImpl.postFilm(film);
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
        filmDaoImpl.postFilm(film);
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
        filmDaoImpl.putFilm(film1);
        AssertionsForClassTypes.assertThat(filmDaoImpl.getFilmId(film1.getId()))
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
        filmDaoImpl.postFilm(film);
        AssertionsForClassTypes.assertThat(filmDaoImpl.getFilmId(film.getId()))
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
        Assertions.assertThatThrownBy(() -> filmDaoImpl.putFilm(film));
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
        filmDaoImpl.postFilm(film);
        Collection<Film> films = filmDaoImpl.getAllFilm();
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

        userDaoImpl.postUser(user);
        filmDaoImpl.postFilm(film);
        filmServiceImpl.addLikeFilm(film.getId(), user.getId());
        Assertions.assertThat(filmServiceImpl.topFilmLike(1).size() == 1);
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

        userDaoImpl.postUser(user);
        userDaoImpl.postUser(user2);
        filmDaoImpl.postFilm(film);
        filmDaoImpl.postFilm(film2);
        filmServiceImpl.addLikeFilm(film.getId(), user.getId());
        filmServiceImpl.addLikeFilm(film.getId(), user2.getId());
        filmServiceImpl.addLikeFilm(film2.getId(), user.getId());
        Assertions.assertThat(filmServiceImpl.topFilmLike(2).get(1).getName().equals(film.getName()));

    }

}