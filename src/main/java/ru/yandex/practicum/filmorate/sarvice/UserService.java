package ru.yandex.practicum.filmorate.sarvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    @Autowired
    InMemoryUserStorage users;


    public void putFriend(int idUser, int idFriend) throws NotFoundExeption {

        if (users.getUserId(idUser) != null && users.getUserId(idFriend) != null) {
            log.debug("{} теперь друг для {}", users.getUserId(idUser).getName(), users.getUserId(idFriend).getName());
            users.getUserId(idUser).addFriend(idFriend);
            users.getUserId(idFriend).addFriend(idUser);
        } else {
            throw new NotFoundExeption("Неверный ID пользователя");
        }
    }

    public void deleteFriend(int idUser, int idFriend) throws NotFoundExeption {
        log.debug("{} теперь не друг с  {}", users.getUserId(idUser).getName(), users.getUserId(idFriend).getName());
        users.getUserId(idUser).deleteFriend(idFriend);
        users.getUserId(idFriend).deleteFriend(idUser);
    }

    public List<User> listOfMutualFriends(int idUser1, int idUser2) throws NotFoundExeption {
        List<User> mutualFriends = new ArrayList<>();
        for (Integer id : users.getUserId(idUser1).getFriends()) {
            if (users.getUserId(idUser2).getFriends().contains(id)) {
                mutualFriends.add(users.getUserId(id));
            }
        }
        log.debug("Вывели список общий друзей у {} и {}"
                , users.getUserId(idUser1).getName(), users.getUserId(idUser2).getName());
        return mutualFriends;
    }

    public List<User> listFriendUserId(int id) throws NotFoundExeption {
        List<User> listFriend = new ArrayList<>();
        for (Integer userId : users.getUserId(id).getFriends()) {
            listFriend.add(users.getUserId(userId));
        }
        log.debug("Получили список друзей у {}", users.getUserId(id).getName());
        return listFriend;
    }
}
