package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    private UserDbStorage userDbStorage;

    public UserService(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public boolean putFriend(int idUser, int idFriend) throws NotFoundExeption {
        if (containsUser(idUser) && containsUser(idFriend)) {
            boolean st1 = getFriendStatus(idUser, idFriend);
            boolean st2 = getFriendStatus(idFriend, idUser);
            if (!st1 && !st2) {
                String sqlQuery = "INSERT INTO friends_user (user_id, friend_id, status) " +
                        "values (?, ?, ?)";
                jdbcTemplate.update(sqlQuery, idUser, idFriend, true);
            } else if (!st1 && st2) {
                String sqlQuery = "INSERT INTO friends_user (user_id, friend_id, status) " +
                        "values (?, ?, ?)";
                jdbcTemplate.update(sqlQuery, idUser, idFriend, false);
                sqlQuery = "UPDATE friends_user SET status = ? " +
                        "WHERE user_id = ? AND friend_id =?";
                jdbcTemplate.update(sqlQuery, 2, idFriend, idUser);
            }
            return true;
        }
        return false;
    }

    public void deleteFriend(int idUser, int idFriend) {
        String sql = "DELETE friends_user WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, idUser, idFriend);
    }

    public List<User> listOfMutualFriends(int idUser1, int idUser2) {
        String sql = "SELECT * FROM users " +
                "WHERE user_id IN " +
                "(SELECT friend_id FROM friends_user WHERE user_id = ?) ORDER BY user_id";
        List<User> usersList = jdbcTemplate.query(sql, this::mapRowToUser, idUser1);
        String sql1 = "SELECT * FROM users " +
                "WHERE user_id IN " +
                "(SELECT friend_id FROM friends_user WHERE user_id = ?) ORDER BY user_id";
        List<User> usersList1 = jdbcTemplate.query(sql1, this::mapRowToUser, idUser2);
        return usersList.stream()
                .filter(usersList1::contains)
                .collect(Collectors.toList());
    }

    public List<User> listFriendUserId(int id) throws NotFoundExeption {
        String sql = "SELECT * FROM friends_user WHERE user_id=? AND status=true";

        List<UserFriend> userFriends = jdbcTemplate.query(sql, this::mapRowToUserFriend, id);
        List<User> users = new ArrayList<>();
        for (UserFriend n : userFriends) {
            users.add(userDbStorage.getUserId(n.getFriendId()));
        }
        return users;
    }

    public UserFriend mapRowToUserFriend(ResultSet resultSet, int rowNum) throws SQLException {
        return UserFriend.builder()
                .userId(resultSet.getInt("user_id"))
                .friendId(resultSet.getInt("friend_id"))
                .status(resultSet.getBoolean("status"))
                .build();
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

    public boolean containsUser(int id) throws NotFoundExeption {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id=?", id);
        if (userRows.next()) {
            return true;
        } else {
            throw new NotFoundExeption("Нет такого юзера");
        }
    }

    private boolean getFriendStatus(int id, int friendId) {
        String sql = "SELECT status FROM friends_user WHERE user_id=? AND friend_id=?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id, friendId);
        if (rs.next()) {
            return rs.getBoolean("status");
        } else {
            return false;
        }
    }
}
