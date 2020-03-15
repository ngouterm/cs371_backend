package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.ExternalDatabase;
import com.example.thebigmoviebackend.model.Movie;

import java.util.ArrayList;

public interface DatabaseService {
    public ArrayList<ExternalDatabase> getAvailableExternalDatabases();

    public ArrayList<Movie> getMovieResults(String query, ArrayList<ExternalDatabase> databasesToSearch);


}
