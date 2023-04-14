package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User postUser(User user) throws ValidationException {
        userValidator(user);

        String sqlQuery = "INSERT INTO users (email, name, login, birthday)" +
                " VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return user;
    }

    @Override
    public User putUser(User user) throws ValidationException, NotFoundExeption {
        String sqlQuere = "UPDATE users SET " +
                   " login = ?, name = ?, email = ?, birthday = ? WHERE user_id = ?";
          jdbcTemplate.update(sqlQuere,user.getLogin(),user.getName(), user.getEmail(),
                   Date.valueOf(user.getBirthday()), user.getId());
           return getUserId(user.getId());
    }

    @Override
    public void deleteUser(Integer id) {
        String sqlQuery = "DETELE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User getUserId(Integer id) throws NotFoundExeption {
       try {
           String sqlQuery = "SELECT * FROM users WHERE user_id=?";
           return jdbcTemplate.queryForObject(sqlQuery,this::mapRowToUser, id);
       } catch (Exception e) {
           throw  new NotFoundExeption("Нет такого еюзера");
       }

    }

    @Override
    public List<User> getAllUser() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    private void userValidator(User user) throws ValidationException {
        LocalDate nowDate = LocalDate.now();
        if (user.getBirthday().isAfter(nowDate)) {
            log.warn("Дата рождения написана неверно: {}", user.getBirthday());
            throw new ValidationException("Дата рождения неможет быть в будущем!");
        }

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Неправильно написан емайл или пустой: {}", user.getEmail());
            throw new ValidationException("Введен не емайл!");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Неправильно написан логин или пустой: {}", user.getLogin());
            throw new ValidationException("Логин вводится без пробелов!");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("Имя было пустым, поэтому применили к полю name начение поля login");
        }
    }

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
