package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.ExternalDatabase;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.storage.DataType;
import com.example.thebigmoviebackend.storage.DatabaseManager;

import java.util.ArrayList;

public class InternalDatabaseService implements DatabaseService {
    DatabaseManager databaseManager = DatabaseManager.getInstance();

    @Override
    public ArrayList<ExternalDatabase> getAvailableExternalDatabases() {
        return null;
    }

    @Override
    public ArrayList<Movie> getMovieResults(String query, ArrayList<ExternalDatabase> databasesToSearch) {
        return databaseManager.search(DataType.MOVIE, query);
    }
}
