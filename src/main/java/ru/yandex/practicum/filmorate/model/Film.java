package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {

   private Set<Integer> likeUser;
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

    public void addListLikeUser(int id){
        likeUser.add(id);
    }

    public void removeLike(int id){
        likeUser.remove(id);
    }
}
