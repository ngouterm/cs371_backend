package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.Movie;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TMDBService implements DatabaseService {
    TmdbApi tmdbApi = new TmdbApi("5d93339e310ea272a10acef74057cec2");

    @Override
    public ArrayList<Movie> getMovieResults(String query) {
        MovieResultsPage resultsPage = tmdbApi.getSearch().searchMovie(query, null, "en", false, 0);
        ArrayList<MovieDb> movieDbs = (ArrayList<MovieDb>) resultsPage.getResults();
        return convertModel(movieDbs);
    }

    private ArrayList<Movie> convertModel(ArrayList<MovieDb> movieDbs) {
        ArrayList<Movie> results = new ArrayList<>();
        for (MovieDb movieDb: movieDbs) {
            Movie movie = new Movie();
//            TODO: finish conversion
            movie.setTitle(movieDb.getTitle());
//            movie.setAdult(movieDb.isAdult());
            results.add(movie);
        }
        return results;
    }
}
