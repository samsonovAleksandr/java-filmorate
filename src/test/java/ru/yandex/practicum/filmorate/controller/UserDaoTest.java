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
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.dao.UserDaoImpl;

import java.time.LocalDate;
import java.util.Collection;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDaoTest {


    @Autowired
    private final UserServiceImpl userServiceImpl;
    private final UserDaoImpl userDaoImpl;

    @Test
    void postUser() throws ValidationException {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDaoImpl.postUser(user);
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
        userDaoImpl.postUser(user);
        AssertionsForClassTypes.assertThat(userDaoImpl.getUserId(user.getId()))
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
        userDaoImpl.postUser(user);
        User userUp = User.builder()
                .id(1)
                .email("simaUp@mail.ru")
                .login("simaUp")
                .name("AlexUp")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDaoImpl.putUser(userUp);
        AssertionsForClassTypes.assertThat(userDaoImpl.getUserId(userUp.getId()))
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
        Assertions.assertThatThrownBy(() -> userDaoImpl.putUser(user));
    }

    @Test
    void addFriendUser() throws ValidationException, NotFoundExeption {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDaoImpl.postUser(user);
        User user2 = User.builder()
                .email("sima2@mail.ru")
                .login("sima2")
                .name("Alex2")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDaoImpl.postUser(user2);
        userServiceImpl.putFriend(user.getId(), user2.getId());
        Assertions.assertThat(userServiceImpl.listFriendUserId(user.getId())).isNotNull();
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

        userDaoImpl.postUser(user);
        userDaoImpl.postUser(user2);
        userDaoImpl.postUser(user3);

        userServiceImpl.putFriend(user.getId(), user2.getId());
        userServiceImpl.putFriend(user2.getId(), user3.getId());
        Assertions.assertThat(userServiceImpl.listOfMutualFriends(user.getId(), user2.getId()).size() == 1);
    }

    @Test
    void getAllUsers() throws ValidationException {
        User user = User.builder()
                .email("sima@mail.ru")
                .login("sima")
                .name("Alex")
                .birthday(LocalDate.of(1994, 11, 2))
                .build();
        userDaoImpl.postUser(user);
        Collection<User> users = userDaoImpl.getAllUser();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
        Assertions.assertThat(users).extracting("login").contains(user.getLogin());
    }
}