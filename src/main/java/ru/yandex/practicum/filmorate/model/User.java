package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {

    private Set<Integer> friends;

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(int id){
        friends.add(id);
    }

    public void deleteFriend(int id){
        friends.remove(id);
    }

}
