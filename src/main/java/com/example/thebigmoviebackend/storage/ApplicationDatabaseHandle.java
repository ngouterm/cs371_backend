package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.Comment;
import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface to ensure all databases are able to utalize the same functions
 */
interface ApplicationDatabaseHandle {

    /**
     * @return return an active database connection
     */
    Connection connect();

    /**
     * @param dataType enum that impacts where we will search
     * @param data the search string
     * @return returns a generic List of the results
     */
    List<?> search(DataType dataType, String data);

    /**
     * @param uuid Pass the MediaList of the movie you wish to get
     * @return returns a MediaList from the local database that the uuid matches
     * Only one MediaList or null will be returned
     */
    MediaList getMediaListByUUID(String uuid);

    /**
     * @param uuid Pass the uuid of the movie you wish to get
     * @return returns a movie from the local database that the uuid matches
     * Only one movie or null will be returned
     */
    Movie getMovieByUUID(String uuid);

    /**
     * @param uuid Pass the uuid of the user you wish to get
     * @return returns a user from the local database that the uuid matches
     * Only one user or null will be returned
     */
    User getUserByUUID(String uuid);

    /**
     * @param data List of media that is to be saved
     * Save all media that is passed
     */
    void saveMovies(List<Movie> data);

    /**
     * @param data User information to save
     * Save a single user to the database
     */
    void saveUser(User data);

    /**
     * @param data Searches the database for this username
     * @return returns a single users based on username search results
     * Returns a single user
     */
    User getUserByUsername(String data);

    /**
     * @param user User who owns the lsits
     * @return ArrayList of all the lists of the passed user
     * return an List of all the lists for the passed user
     */
    List<MediaList> getLists(User user);

    void saveComment(Comment comment);

    ArrayList<Comment> getUserComments(User user);

    ArrayList<Comment> getMovieComments(Movie movie);

    ArrayList<Comment> getCommentByUUID(String commentUUID);

    /**
     * @param mediaList MediaList to save
     */
    void saveList(MediaList mediaList);

    /**
     * @param mediaList MediaList to delete
     */
    void deleteList(MediaList mediaList);

    /**
     * @return List of all users
     */
    List<User> getAllUsers();

    /**
     * @return List of all MediaLists
     */
    List<MediaList> getAllLists();

    /**
     * @return List of all Media
     */
    List<Movie> getAllMedia();

}
