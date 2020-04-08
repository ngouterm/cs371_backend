package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
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
        for (DatabaseService databaseService : externalDatabases) {
            results.addAll(databaseService.getMovieResults(query));
        }
        return results;
    }

    public HashMap<DatabaseService, ArrayList<Movie>> getMovieResults(String query, ArrayList<DatabaseService> dbs) {
        HashMap<DatabaseService, ArrayList<Movie>> results = new HashMap<>();
        for (DatabaseService databaseService: dbs) {
            results.put(databaseService, databaseService.getMovieResults(query));
        }
        return results;
    }

    public void saveMovies(ArrayList<Movie> movies) {
        internalDatabaseService.saveMovies(movies);
    }

    public HashMap<String, DatabaseService> getDatabases() {
        HashMap<String, DatabaseService> dbs = new HashMap<>();
        dbs.put(internalDatabaseService.toString(), internalDatabaseService);
        for (DatabaseService ds: externalDatabases) {
            dbs.put(ds.toString(), ds);
        }
        return dbs;
    }

    public ArrayList<String> getExternalDatabases() {
        return (ArrayList<String>) externalDatabases.stream().map(DatabaseService::toString).collect(Collectors.toList());
    }

    public ArrayList<Movie> getAllMovies() {
        return internalDatabaseService.getAllMovies();
    }

    public ArrayList<MediaList> getAllLists() {
        return internalDatabaseService.getAllLists();
    }
}
