package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;
import com.example.thebigmoviebackend.storage.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class UserService {
    DatabaseManager databaseManager = DatabaseManager.getInstance();

    //    ArrayList<User> users = new ArrayList<>();
//    ArrayList<MediaList> mediaLists = new ArrayList<>();

//    {
//        //Add reserved usernames.
//        users.add(new User("q"));
//    }

    public boolean createUser(String username) {
        //TODO: prompt for password, email
        User user = databaseManager.getUser(username);
        if (user == null) {
            user = new User(username);
            databaseManager.saveUser(user);
            return true;
        }
        return false;
    }

    public User login(String username, String password) {
        //TODO: implement password check
        return databaseManager.getUser(username);
    }

    public ArrayList<MediaList> getMediaLists(User user) {
        return databaseManager.getLists(user);
    }

    public MediaList createMediaList(User user, String name) {
        //TODO: check for dupes
        MediaList mediaList = new MediaList(user, name);
        databaseManager.saveList(mediaList);
        return mediaList;
    }

    public MediaList getMediaList(User user, String name) {
        return databaseManager.getLists(user).stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList()).get(0);
    }

    public void saveMediaList(MediaList mediaList) {
        databaseManager.saveList(mediaList);
    }

    public void deleteMediaList(User user, MediaList mediaList) {
        databaseManager.deleteList(mediaList);
    }

}
