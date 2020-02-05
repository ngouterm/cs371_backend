package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.ExternalDatabase;
import com.example.thebigmoviebackend.model.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ExternalDatabaseService implements DatabaseService {
    @Override
    public ArrayList<ExternalDatabase> getAvailableExternalDatabases() {
        //TODO: implement
        ArrayList<ExternalDatabase> externalDatabases = new ArrayList<>();
        externalDatabases.add(new ExternalDatabase("themoviedb"));
        return externalDatabases;
    }

    @Override
    public ArrayList<Movie> getMovieResults(String query, ArrayList<ExternalDatabase> databasesToSearch) {
        ArrayList<Movie> results = new ArrayList<>();
//        results.add(new Movie("Ghost in the Shell"));
        return results;
    }
}
