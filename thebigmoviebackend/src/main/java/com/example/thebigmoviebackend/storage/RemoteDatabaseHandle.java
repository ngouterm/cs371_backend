package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.Movie;

import java.sql.Connection;
import java.util.ArrayList;

class RemoteDatabaseHandle implements DatabaseHandle {
    @Override
    public Connection connect() {

        return null;
    }

    @Override
    public ArrayList<Movie> search(DataType dataType, String data) {
        return null;
    }

}
