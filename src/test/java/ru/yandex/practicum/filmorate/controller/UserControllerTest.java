package ru.yandex.practicum.filmorate.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {
    private final UserController userController = new UserController();



    @Test
    public void createAndGetAllUsers() throws Exception, ValidationException {
        User user = new User("ls@ls.ru","sima","alex",LocalDate.of(1999,10,11));
        user.setId(1);
       User user1 =  userController.postUser(user);
       Assert.assertEquals(user, user1);
       Assert.assertEquals(userController.getUsers().size(), 1);
    }

    @Test
    public void createUserIfEmailIsEmpty() throws ValidationException {
        User user = new User("","sima","alex",LocalDate.of(1999,10,11));
        Assertions.assertThrows(ValidationException.class, () -> {userController.postUser(user);});
    }
    @Test
    public void createUserIfNameIsEmpty() throws ValidationException {
        User user = new User("ls@ls.ru","sima","",LocalDate.of(1999,10,11));
        userController.postUser(user);
        Assertions.assertEquals(user.getName(), user.getLogin());
    }
    @Test
    public void createUserIfLoginIsEmpty() throws ValidationException {
        User user = new User("ls@ls.ru","","alex",LocalDate.of(1999,10,11));
        Assertions.assertThrows(ValidationException.class, () -> {userController.postUser(user);});
    }
    @Test
    public void createUserIfBirthdayAfterDateNow() throws ValidationException {
        User user = new User("ls@ls.ru","sima","alex",LocalDate.of(2024,10,11));
        Assertions.assertThrows(ValidationException.class, () -> {userController.postUser(user);});
    }

    @Test
    public void userNull() throws ValidationException {
        User user = new User(null,null,null,null);
        Assertions.assertThrows(NullPointerException.class, () -> {userController.postUser(user);});
    }
}
