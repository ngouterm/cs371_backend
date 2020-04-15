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

/**
 * Rest controller is a resource controller that handles the https requests and directs them to that mapped methods
 */
@RestController
public class RestfulService {
    UserService userService = new UserService();
    MixedDatabaseService mixedDatabaseService = new MixedDatabaseService();

    /**
     * @return All users in the application database
     */
    @GetMapping("/user/")
    public ArrayList<User> user() {
        return userService.getAllUsers();
    }

    /**
     * @param id UUID that defines a user
     * @return single User that matches the uuid
     */
    @GetMapping("/user/{id}")
    public User user(@PathVariable String id) {
        return userService.getUser(id);
    }

    /**
     * @param username user search string i.e. username
     * @return first matching user
     */
    @GetMapping("/user")
    public ArrayList<User> userSearch(@RequestParam String username) {
        return userService.searchUsers(username);
    }

    /**
     * @return All Movies across application databases
     */
    @GetMapping("/movie/")
    public ArrayList<Movie> movie() {
        return mixedDatabaseService.getAllMovies();
    }

    /**
     * @param id Pass the uuid of the movie you wish to get
     * @return returns a movie from the local database that the uuid matches
     * Only one movie or null will be returned
     */
    @GetMapping("/movie/{id}")
    public Movie movie(@PathVariable String id) {
        return mixedDatabaseService.getMovie(id);
    }


    /**
     * @param title Title of movie to search the database for
     * @return ArrayList of Movie of the movies that contain the search sting in their title
     */
    @GetMapping(value = "/movie", params = {"title"})
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

    /**
     * @param title Title of movie to search the databases for
     * @param dbs   Databases to search
     * @return HashMap of the database service and the movie
     * To keep track of what database service the movie was found
     */
    @GetMapping(value = "/movie", params = {"title", "dbs"})
    public HashMap<DatabaseService, ArrayList<Movie>> searchMovie(@RequestParam String title, @RequestParam String dbs) {
        return mixedDatabaseService.getMovieResults(title, databaseServices(dbs));
    }

    /**
     * @return All MediaLists across application databases
     */
    @GetMapping("/list/")
    public ArrayList<MediaList> list() {
        return mixedDatabaseService.getAllLists();
    }

    /**
     * @param id Pass the uuid of the MediaList you wish to get
     * @return returns a MediaList from the local database that the uuid matches
     * Only one MediaList or null will be returned
     */
    @GetMapping("/list/{id}")
    public MediaList list(@PathVariable String id) {
        return mixedDatabaseService.getMediaList(id);
    }

    /**
     * @param userid userid who lists you want
     * @return ArrayList of MediaLists for the User passed across application databases
     */
    @GetMapping(value = "/user/{userid}/list", params = {"userid"})
    public ArrayList<MediaList> userList(@PathVariable String userid) {
        User user = userService.getUser(userid);
        return userService.getMediaLists(user);
    }

    /**
     * @param userid userid who lists you want
     * @param name   name of the list you wish to get
     * @return Medialist requested
     */
    @GetMapping(value = "/user/{userid}/list", params = {"userid", "name"})
    public MediaList userList(@PathVariable String userid, @RequestParam String name) {
        User user = userService.getUser(userid);
        return userService.getMediaList(user, name);
    }
}
