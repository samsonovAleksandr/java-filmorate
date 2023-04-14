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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {


    @Autowired
    private final UserService userService;
    private final UserDbStorage userDbStorage;

    @Test
    void postUser() throws ValidationException {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDbStorage.postUser(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }

    @Test
    void findUserById() throws ValidationException, NotFoundExeption {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDbStorage.postUser(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUserId(user.getId()))
                .hasFieldOrPropertyWithValue("id", user.getId());
    }

    @Test
    void putUser() throws ValidationException, NotFoundExeption {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDbStorage.postUser(user);
        User userUp = User.builder()
                .id(1)
                .email("simaUp@mail.ru")
                .login("simaUp")
                .name("AlexUp")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDbStorage.putUser(userUp);
        AssertionsForClassTypes.assertThat(userDbStorage.getUserId(userUp.getId()))
                .hasFieldOrPropertyWithValue("id", userUp.getId());
    }

    @Test
    void notFoundUser() {
        User user = User.builder()
                .id(9999)
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        Assertions.assertThatThrownBy(() -> userDbStorage.putUser(user));
    }

    @Test
    void addFriendUser() throws ValidationException, NotFoundExeption {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDbStorage.postUser(user);
        User user2 = User.builder()
                .email("sima2@mail.ru")
                .login("sima2")
                .name("Alex2")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDbStorage.postUser(user2);
        userService.putFriend(user.getId(), user2.getId());
        Assertions.assertThat(userService.listFriendUserId(user.getId())).isNotNull();
    }

    @Test
    void getCommonFriendship() throws ValidationException, NotFoundExeption {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        User user2 = User.builder()
                .email("sima2@mail.ru")
                .login("sima2")
                .name("Alex2")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        User user3 = User.builder()
                .email("sima3@mail.ru")
                .login("sima3")
                .name("Alex3")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();

        userDbStorage.postUser(user);
        userDbStorage.postUser(user2);
        userDbStorage.postUser(user3);

        userService.putFriend(user.getId(), user2.getId());
        userService.putFriend(user2.getId(), user3.getId());
        Assertions.assertThat(userService.listOfMutualFriends(user.getId(), user2.getId()).size() == 1);
    }

    @Test
    void getAllUsers() throws ValidationException {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDbStorage.postUser(user);
        Collection<User> users = userDbStorage.getAllUser();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
        Assertions.assertThat(users).extracting("login").contains(user.getLogin());
    }
}