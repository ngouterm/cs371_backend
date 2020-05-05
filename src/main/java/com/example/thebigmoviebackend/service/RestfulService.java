package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.Comment;
import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;
import com.example.thebigmoviebackend.storage.DatabaseManager;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Rest controller is a resource controller that handles the https requests and directs them to that mapped methods
 */
@RestController
public class RestfulService {
    UserService userService = new UserService();
    MixedDatabaseService mixedDatabaseService = new MixedDatabaseService();
    DatabaseManager databaseManager = DatabaseManager.getInstance();

    /**
     * @return All users in the application database
     */
    @GetMapping("/user/")
    public ArrayList<User> user() {
        return userService.getAllUsers();
    }

    /**
     * @param user user to create
     * @return whether the user was created
     */
    @PostMapping(value = "/user/")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        user = new User(user.getUsername());
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
     * Creates a list of movies
     *
     * @param list list to save
     * @return whether the list was created
     */
    @PostMapping("list/")
    public ResponseEntity<String> createList(@RequestBody MediaList list) {

        list = new MediaList(list.getUser(), list.getName(), list.getMovies());
        userService.saveMediaList(list);
        return new ResponseEntity<>(HttpStatus.CREATED);
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

    /**
     * Deletes a medialist
     *
     * @param mediaList list to delete
     * @param userid    user auth for the medialist
     * @return whether the list was deleted.
     */
    @DeleteMapping("/list")
    public boolean deleteList(@RequestBody MediaList mediaList, @RequestParam String userid) {
        User user = userService.getUser(userid);
        if (mediaList.getUser().equals(user)) {
            userService.deleteMediaList(user, mediaList);
            return true;
        }
        return false;
    }

    @GetMapping(value = "/comment", params = {"userid"})
    public ArrayList<Comment> userComments(@RequestParam String userid) {
        return databaseManager.getUserComments(userService.getUser(userid));
    }

    @GetMapping(value = "/comment", params = {"movieid"})
    public ArrayList<Comment> movieComments(@RequestParam String movieid) {
        return databaseManager.getMovieComments(databaseManager.getMovieByUUID(movieid));
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<String> createComment(@RequestBody Comment comment) {
        comment = new Comment(comment.getUserUUID(), comment.getMovieUUID(), comment.getComment());
        databaseManager.saveComment(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
