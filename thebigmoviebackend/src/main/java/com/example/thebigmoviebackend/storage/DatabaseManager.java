package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.util.ArrayList;
import java.util.Collection;

public class DatabaseManager implements StorageManager {

    private static DatabaseManager instance;
    LocalDatabaseHandle localDatabaseHandle = new LocalDatabaseHandle();
    ArrayList<RemoteDatabaseHandle> remoteDatabaseHandles = new ArrayList<RemoteDatabaseHandle>();

    private DatabaseManager(){
        connectDatabases();
    }

    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void createDatabaseConnection(){

    }

    private void connectDatabases(){

        localDatabaseHandle = new RDSDatabaseHandle();
        localDatabaseHandle.connect();

        for(RemoteDatabaseHandle databaseHandle : remoteDatabaseHandles){
            databaseHandle.connect();
        }
    }

    public ArrayList<Movie> search(DataType dataType, String data) {
        ArrayList<Movie> results = new ArrayList<>();
        results.addAll(localDatabaseHandle.search(dataType, data));
        for(RemoteDatabaseHandle remoteDatabaseHandle : remoteDatabaseHandles){
            results.addAll(remoteDatabaseHandle.search(dataType, data));
        }
        return null;
    }

    public User getUser(String data) {
        return localDatabaseHandle.getUser(data);
    }

    public void save(DataType dataType, String data) {
        localDatabaseHandle.save(dataType, data);
    }

    public void saveMovies(ArrayList<Movie> data) {
        localDatabaseHandle.saveMovies(data);
    }

    public void saveUser(User data) {
        localDatabaseHandle.saveUser(data);
    }


}
