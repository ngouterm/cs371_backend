package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class RestfulService {
    UserService userService = new UserService();
    MixedDatabaseService mixedDatabaseService = new MixedDatabaseService();

    @GetMapping("/user/")
    public ArrayList<User> user() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/user")
    public ArrayList<User> userSearch(@RequestParam String username) {
        return userService.searchUsers(username);
    }

    @GetMapping("/movie/")
    public ArrayList<Movie> movie() {
        return mixedDatabaseService.getAllMovies();
    }

    @GetMapping("/movie/{id}")
    public Movie movie(@PathVariable String id) {
        return mixedDatabaseService.getMovie(id);
    }

    @GetMapping("/movie")
    public ArrayList<Movie> searchMovie(@RequestParam String title) {
        return mixedDatabaseService.getMovieResults(title);
    }

    private final String allDatabases = "all";

    ArrayList<DatabaseService> databaseServices(String databases) {
        if (databases.equals(allDatabases)) {
            return new ArrayList<>(mixedDatabaseService.getDatabases().values());
        }

        ArrayList<DatabaseService> services = new ArrayList<>();
        String[] dbs = databases.split(",");
        for (String db : dbs) {
            DatabaseService service = mixedDatabaseService.getDatabases().get(db);
            if (service != null) {
                services.add(service);
            }
        }

        return services;
    }

    @GetMapping("/movie")
    public HashMap<DatabaseService, ArrayList<Movie>> searchMovie(@RequestParam String title, @RequestParam String dbs) {
        return mixedDatabaseService.getMovieResults(title, databaseServices(dbs));
    }

    @GetMapping("/list/")
    public ArrayList<MediaList> list() {
        return mixedDatabaseService.getAllLists();
    }

    @GetMapping("/list/{id}")
    public MediaList list(@PathVariable String id) {
        return mixedDatabaseService.getMediaList(id);
    }

    @GetMapping("/user/{userid}/list")
    public ArrayList<MediaList> userList(@PathVariable String userid) {
        User user = userService.getUser(userid);
        return userService.getMediaLists(user);
    }

    @GetMapping("/user/{userid}/list")
    public MediaList userList(@PathVariable String userid, @RequestParam String name) {
        User user = userService.getUser(userid);
        return userService.getMediaList(user, name);
    }
}
