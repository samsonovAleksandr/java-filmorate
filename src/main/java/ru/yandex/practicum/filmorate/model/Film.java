package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {

    private Set<Integer> likeUser = new HashSet<>();
    private List<Integer> genre_id = new ArrayList<>();
    private String rating;
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addListLikeUser(int id) {
        likeUser.add(id);
    }

    public void removeLike(int id) {
        likeUser.remove(id);
    }
}
