package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    ArrayList<User> users = new ArrayList<>();

    {
        //Add reserved usernames.
        users.add(new User("q"));
    }

    public boolean createUser(String username) {
        //TODO: prompt for password, email
        User user = new User(username);
        if (!users.contains(user)) {
            users.add(user);
            return true;
        }
        return false;
    }

    public User login(String username, String password) {
        //TODO: implement password check
        User user = new User(username);
        if (users.contains(user)) {
            return user;
        } else {
            return null;
        }
    }
}
