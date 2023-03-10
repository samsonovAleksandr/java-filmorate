package ru.yandex.practicum.filmorate.sarvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    @Autowired
    InMemoryFilmStorage films;

    public void addLikeFilm(int idFilm, int idUser){
        films.getFilmId(idFilm).addListLikeUser(idUser);

    }

    public void deleteLike(int idFilm, int idUser){
        films.getFilmId(idFilm).removeLike(idUser);
    }

    public List<Film> topFilmLike(){
        List<Film> filmsList = films.getAllFilm();
        filmsList.sort(new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                if (o1.getLikeUser().size() == o2.getLikeUser().size()) return 0;
                else if (o1.getLikeUser().size()> o2.getLikeUser().size()) return 1;
                else return -1;
            }

        });
        List<Film> topFilm = null;
        for (int i = 0; i <= 10; i++) {
            topFilm.add(filmsList.get(i));
        }

        return topFilm;
    }
}
