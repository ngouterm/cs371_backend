package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.util.ArrayList;

abstract class AbstractStorageHandle  implements  StorageHandle{

    public void prepare(){}

    public void save(DataType dataType, String data){}

    public void saveMovies(ArrayList<Movie> data){}

    public void saveUser(User data){}
}
