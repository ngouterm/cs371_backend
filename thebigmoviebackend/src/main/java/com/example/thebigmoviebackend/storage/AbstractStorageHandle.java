package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.util.ArrayList;

abstract class AbstractStorageHandle implements StorageHandle {

    public abstract void prepare();

    public abstract void save(DataType dataType, String data);

    public abstract void saveMovies(ArrayList<Movie> data);

    public abstract void saveList(MediaList mediaList);

    public abstract User getUser(String data);

    public abstract ArrayList<MediaList> getLists(User user);

    public abstract void deleteList(MediaList mediaList);
}
