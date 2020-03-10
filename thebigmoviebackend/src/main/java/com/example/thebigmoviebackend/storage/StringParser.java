package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.Movie;

import java.util.ArrayList;

public class StringParser {

    private static StringParser instance;

    private StringParser(){}

    public static StringParser getInstance(){
        if(instance == null){
            instance = new StringParser();
        }
        return instance;
    }

    public ArrayList<Movie> parseMovie(String movieList){
        return new ArrayList<>();
    }

}
