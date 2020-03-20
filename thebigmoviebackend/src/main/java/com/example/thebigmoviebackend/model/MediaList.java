package com.example.thebigmoviebackend.model;

import java.util.ArrayList;

public class MediaList {
    User user;
    ArrayList<Movie> movies;
    String name;

    public MediaList(User user, String name) {
        this.user = user;
        this.movies = new ArrayList<>();
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
