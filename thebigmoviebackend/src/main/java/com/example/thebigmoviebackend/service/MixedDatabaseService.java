package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.Movie;

import java.util.ArrayList;

public class MixedDatabaseService implements DatabaseService {
    InternalDatabaseService internalDatabaseService = new InternalDatabaseService();
    TMDBService TMDBService = new TMDBService();
    @Override
    public ArrayList<Movie> getMovieResults(String query) {
        ArrayList<Movie> results = internalDatabaseService.getMovieResults(query);
        results.addAll(TMDBService.getMovieResults(query));
        return results;
    }

    public void saveMovies(ArrayList<Movie> movies) {
        internalDatabaseService.saveMovies(movies);
    }
}
