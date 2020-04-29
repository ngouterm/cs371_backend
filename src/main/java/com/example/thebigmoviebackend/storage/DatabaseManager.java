package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.Comment;
import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance of the primary connection to the databases
 * At this time we only connect to a AWS RDS database
 * This is so one action can action all connected application databases
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    ApplicationDatabaseHandle localDatabaseHandle;

    private DatabaseManager() {
        connectDatabases();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void connectDatabases() {

        localDatabaseHandle = new RDSDatabaseHandle();
        localDatabaseHandle.connect();

    }

    /**
     * @param dataType enum that impacts where we will search
     * @param data the search string
     * @return returns a generic Arraylist of the results
     * Searches across all application databases
     */
    public ArrayList<?> search(DataType dataType, String data) {
        ArrayList<?> results = new ArrayList<>(localDatabaseHandle.search(dataType, data));
        return results;
    }

    /**
     * @param data user search string i.e. username
     * @return first matching user
     */
    public User getUser(String data) {
        return localDatabaseHandle.getUserByUsername(data);
    }

    /**
     * @param data list of media that you want to save to application databases
     */
    public void saveMovies(List<Movie> data) {
        localDatabaseHandle.saveMovies(data);
    }

    /**
     * @param data User to save to application databases
     */
    public void saveUser(User data) {
        localDatabaseHandle.saveUser(data);
    }

    /**
     * @param mediaList MediaList to save to application databases
     */
    public void saveList(MediaList mediaList) {
        localDatabaseHandle.saveList(mediaList);
    }

    /**
     * @param user User who lists you want
     * @return ArrayList of MediaLists for the User passed across application databases
     */
    public ArrayList<MediaList> getLists(User user) {
        ArrayList<MediaList> lists = new ArrayList<>(localDatabaseHandle.getLists(user));
        return lists;
    }

    /**
     * @param mediaList MediaList to from the application databases
     */
    public void deleteList(MediaList mediaList) { localDatabaseHandle.deleteList(mediaList);}

    /**
     * @return All Users across application databases
     */
    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>(localDatabaseHandle.getAllUsers());
        return users;
    }

    /**
     * @return All MediaLists across application databases
     */
    public ArrayList<MediaList> getAllLists(){
        ArrayList<MediaList> lists = new ArrayList<>(localDatabaseHandle.getAllLists());
        return lists;
    }

    /**
     * @return All Media across application databases
     */
    public ArrayList<Movie> getAllMedia(){
        ArrayList<Movie> media = new ArrayList<>(localDatabaseHandle.getAllMedia());
        return media;
    }

    /**
     * @param uuid Pass the uuid of the movie you wish to get
     * @return returns a movie from the local database that the uuid matches
     * Only one movie or null will be returned
     */
    public Movie getMovieByUUID(String uuid){
        Movie movie = localDatabaseHandle.getMovieByUUID(uuid);
        return movie;
    }

    /**
     * @param uuid Pass the uuid of the user you wish to get
     * @return returns a user from the local database that the uuid matches
     * Only one user or null will be returned
     */
    public User getUserByUUID(String uuid){
        User user = localDatabaseHandle.getUserByUUID(uuid);
        return user;
    }

    /**
     * @param uuid Pass the uuid of the MediaList you wish to get
     * @return returns a MediaList from the local database that the uuid matches
     * Only one MediaList or null will be returned
     */
    public MediaList getMediaListByUUID(String uuid){
        MediaList mediaList = localDatabaseHandle.getMediaListByUUID(uuid);
        return mediaList;
    }

    public void saveComment(Comment comment){
        localDatabaseHandle.saveComment(comment);
    }

    public ArrayList<Comment> getUserComments(User user){
        return localDatabaseHandle.getUserComments(user);
    }

    public ArrayList<Comment> getMovieComments(Movie movie){
        return localDatabaseHandle.getMovieComments(movie);
    }

    public ArrayList<Comment> getCommentByUUID(String uuid){
        return localDatabaseHandle.getCommentByUUID(uuid);
    }
}
