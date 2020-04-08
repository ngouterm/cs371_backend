package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static java.util.stream.Collectors.toList;

@RestController
public class RestfulService {
    UserService userService = new UserService();
    MixedDatabaseService mixedDatabaseService = new MixedDatabaseService();

    @GetMapping("/user/{id}")
    public User user(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/movie/{id}")
    public Movie movie(@PathVariable String id) {
        return mixedDatabaseService.getMovie(id);
    }

    @GetMapping("/list/{id}")
    public MediaList list(@PathVariable String id) {
        return mixedDatabaseService.getMediaList(id);
    }
}
