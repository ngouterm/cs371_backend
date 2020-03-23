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
    ArrayList<MediaList> mediaLists = new ArrayList<>();

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
        return (ArrayList<MediaList>) mediaLists.stream().filter(mediaList -> mediaList.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public void createMediaList(User user, String name) {
        //TODO: check for dupes
        MediaList mediaList = new MediaList(user, name);
        mediaLists.add(mediaList);
    }

    public MediaList getMediaList(User user, String name) {
        return mediaLists.stream()
                .filter(mediaList -> mediaList.getUser().equals(user) && mediaList.getName().equals(name))
                .collect(Collectors.toList()).get(0);
    }

}
