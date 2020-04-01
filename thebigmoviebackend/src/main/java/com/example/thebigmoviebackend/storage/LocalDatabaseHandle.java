package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.sql.Connection;
import java.util.ArrayList;

abstract class LocalDatabaseHandle implements DatabaseHandle {

    public LocalDatabaseHandle(){

    }

    @Override
    public Connection connect() {

        return null;
    }

    @Override
    public ArrayList<?> search(DataType dataType, String data) {

        return null;
    }


    public User getUser(String data) {
        return null;
    }

    @Override
    public ArrayList<MediaList> getLists(User user) {
        return null;
    }

    @Override
    public void prepare() {

    }

    @Override
    public void save(DataType dataType, String data) {

    }

    @Override
    public void saveMovies(ArrayList<Movie> data) {

    }

    public void saveUser(User data) {

    }

    @Override
    public void saveList(MediaList mediaList) {

    }

    @Override
    public  void deleteList(MediaList mediaList){}
}
