package com.example.thebigmoviebackend.model;

import java.util.ArrayList;
import java.util.UUID;

public class MediaList {
    User user;
    ArrayList<Movie> movies;
    String name;
    String uuid;
    public MediaList(){};

    public MediaList(User user, String name) {
        this.user = user;
        this.movies = new ArrayList<>();
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
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

    public Movie removeMovie(int index) {
        return movies.remove(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getUUID(){return uuid;}
}
