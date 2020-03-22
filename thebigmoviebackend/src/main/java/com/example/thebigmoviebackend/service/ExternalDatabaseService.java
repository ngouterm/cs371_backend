package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.ExternalDatabase;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.storage.DataAction;
import com.example.thebigmoviebackend.storage.DataType;
import com.example.thebigmoviebackend.storage.DatabaseManager;
import com.example.thebigmoviebackend.storage.StringParser;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ExternalDatabaseService implements DatabaseService {
    TmdbMovies movies = new TmdbApi("5d93339e310ea272a10acef74057cec2").getMovies();
    MovieDb movie = movies.getMovie(5353, "en");

    @Override
    public ArrayList<ExternalDatabase> getAvailableExternalDatabases() {
        //TODO: implement
        ArrayList<ExternalDatabase> externalDatabases = new ArrayList<>();
        externalDatabases.add(new ExternalDatabase("themoviedb"));
        return externalDatabases;
    }

    @Override
    public ArrayList<Movie> getMovieResults(String query, ArrayList<ExternalDatabase> databasesToSearch) {

        ArrayList<Movie> results = DatabaseManager.getInstance().search(DataType.MOVIE, query);
//        results.add(new Movie("Ghost in the Shell"));
        return results;
    }
}
