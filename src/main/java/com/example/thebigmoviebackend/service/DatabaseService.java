package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.Movie;

import java.util.ArrayList;

public interface DatabaseService {
    ArrayList<Movie> getMovieResults(String query);
}
