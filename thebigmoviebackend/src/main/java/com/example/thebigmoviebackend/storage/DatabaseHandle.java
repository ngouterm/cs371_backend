package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Interface to ensure all databases are able to utalize the same functions
 */
interface DatabaseHandle {

    Connection connect();

    ArrayList<?> search(DataType dataType, String data);

    void prepare();

    void save(DataType dataType, String data);

    void saveMovies(ArrayList<Movie> data);

    void saveUser(User data);

    User getUser(String data);

    ArrayList<MediaList> getLists(User user);

    void saveList(MediaList mediaList);

    void deleteList(MediaList mediaList);

    ArrayList<User> getUsers();

    ArrayList<MediaList> getAllLists();

    ArrayList<Movie> getAllMedia();

}
