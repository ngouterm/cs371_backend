package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.ExternalDatabase;
import com.example.thebigmoviebackend.model.Movie;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MixedDatabaseService implements DatabaseService {
    InternalDatabaseService internalDatabaseService = new InternalDatabaseService();
    ArrayList<DatabaseService> externalDatabases = new ArrayList<>();
    {
        //Add new databases here
        externalDatabases.add(new TMDBService());
    }
    @Override
    public ArrayList<Movie> getMovieResults(String query) {
        ArrayList<Movie> results = internalDatabaseService.getMovieResults(query);
        for (DatabaseService databaseService: externalDatabases) {
            results.addAll(databaseService.getMovieResults(query));
        }
        return results;
    }

    public void saveMovies(ArrayList<Movie> movies) {
        internalDatabaseService.saveMovies(movies);
    }

    public ArrayList<String> getExternalDatabases() {
        return (ArrayList<String>) externalDatabases.stream().map(DatabaseService::toString).collect(Collectors.toList());
    }
}
