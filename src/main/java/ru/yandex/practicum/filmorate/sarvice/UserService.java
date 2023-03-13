package ru.yandex.practicum.filmorate.sarvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Set;

@Slf4j
@Service
public class UserService {

    @Autowired
    InMemoryUserStorage users;


    public void putFriend(int idUser, int idFriend){
        log.debug("{} теперь друг для {}", users.getUserId(idUser).getName(), users.getUserId(idFriend).getName());
        users.getUserId(idUser).addFriend(idFriend);
        users.getUserId(idFriend).addFriend(idUser);
    }

    public void deleteFriend(int idUser, int idFriend){
        log.debug("{} теперь не друг с  {}", users.getUserId(idUser).getName(), users.getUserId(idFriend).getName());
        users.getUserId(idUser).deleteFriend(idFriend);
        users.getUserId(idFriend).deleteFriend(idUser);
    }

    public Set<User> listOfMutualFriends(int idUser1, int idUser2){
        Set<User> mutualFriends = null;
        for (Integer id : users.getUserId(idUser1).getFriends()){
            if (users.getUserId(idUser2).getFriends().contains(id)){
                mutualFriends.add(users.getUserId(idUser1));
            }
        }
        log.debug("Вывели список общий друзей у {} и {}"
                ,users.getUserId(idUser1).getName(), users.getUserId(idUser2).getName());
        return mutualFriends;
    }

    public Set<User> listFriendUserId(int id){
        Set<User> listFriend = null;
        for (Integer userId : users.getUserId(id).getFriends()){
            listFriend.add(users.getUserId(userId));
        }
        return listFriend;
    }
}
