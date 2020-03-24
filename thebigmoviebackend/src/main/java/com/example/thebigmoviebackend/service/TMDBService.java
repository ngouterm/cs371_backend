package com.example.thebigmoviebackend.service;

import com.example.thebigmoviebackend.model.Movie;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.IdElement;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
            movie.setTitle(movieDb.getTitle());
            movie.setAdult(movieDb.isAdult());
            movie.setPopularity((double) movieDb.getPopularity());
            movie.setVoteCount(movieDb.getVoteCount());
            movie.setVideo(movieDb.getVideos() != null);
            movie.setPosterPath(movieDb.getPosterPath());
            movie.setID(movieDb.getId());
            movie.setBackgroundPath(movieDb.getBackdropPath());
            movie.setOriginalLanguage(movieDb.getOriginalLanguage());
            movie.setOriginalTitle(movieDb.getOriginalTitle());
//            movie.setGenreIDs(movieDb.getGenres().stream().map(IdElement::getId).collect(Collectors.toList()));
            movie.setVoteAverage((double) movieDb.getVoteAverage());
            movie.setOverview(movieDb.getOverview());
            movie.setReleaseDate(movieDb.getReleaseDate());
            results.add(movie);
        }
        return results;
    }
}
