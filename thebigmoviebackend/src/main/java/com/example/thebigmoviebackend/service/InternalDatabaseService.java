package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.storage.DataType;
import com.example.thebigmoviebackend.storage.DatabaseManager;

import java.util.ArrayList;

public class InternalDatabaseService implements DatabaseService {
    DatabaseManager databaseManager = DatabaseManager.getInstance();

    @Override
    public ArrayList<Movie> getMovieResults(String query) {
        return (ArrayList<Movie>)databaseManager.search(DataType.MOVIE, query);
    }

    public void saveMovies(ArrayList<Movie> movies) {
        databaseManager.saveMovies(movies);
    }

    @Override
    public String toString() {
        return "internal database";
    }
}
