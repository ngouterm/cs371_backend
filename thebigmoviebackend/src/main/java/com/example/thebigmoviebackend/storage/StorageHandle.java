package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.util.ArrayList;

interface StorageHandle {
    void prepare();

    void save(DataType dataType, String data);

    void saveMovies(ArrayList<Movie> data);

    void saveUser(User data);

    User getUser(String data);

    ArrayList<MediaList> getLists(User user);

    void deleteList(MediaList mediaList);
}
