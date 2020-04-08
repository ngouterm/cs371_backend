package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static java.util.stream.Collectors.toList;

@RestController
public class RestfulService {
    UserService userService = new UserService();
    MixedDatabaseService mixedDatabaseService = new MixedDatabaseService();

    @GetMapping("user")
    public ArrayList<User> user(@RequestParam(value = "username") String username) {
        return userService.searchUsers(username);
    }

    @GetMapping("movie")
    public ArrayList<Movie> movie(@RequestParam(value = "title") String title) {
        return mixedDatabaseService.getMovieResults(title);
    }

    @GetMapping("list")
    public MediaList list(@RequestParam(value = "user") String username, @RequestParam(value = "list") String list) {
        return userService.getMediaLists(userService.searchUsers(username).get(0)).stream().filter(m -> m.getName().equals(list)).collect(toList()).get(0);
    }
}
