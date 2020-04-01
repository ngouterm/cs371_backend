package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;
import jdk.internal.jline.internal.Nullable;

import java.util.ArrayList;

/**
 * Instance of the primary connection to the databases
 * At this time we only connect to a AWS RDS database
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    DatabaseHandle localDatabaseHandle;

    private DatabaseManager() {
        connectDatabases();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void connectDatabases() {

        localDatabaseHandle = new RDSDatabaseHandle();
        localDatabaseHandle.connect();

    }

    /**
     * @param dataType enum that impacts where we will search
     * @param data the search string
     * @return returns a generic Arraylist of the results
     */
    public ArrayList<?> search(DataType dataType, String data) {
        ArrayList<?> results = new ArrayList<>(localDatabaseHandle.search(dataType, data));
        return results;
    }

    public User getUser(String data) {
        return localDatabaseHandle.getUser(data);
    }

    public void saveMovies(ArrayList<Movie> data) {
        localDatabaseHandle.saveMovies(data);
    }

    public void saveUser(User data) {
        localDatabaseHandle.saveUser(data);
    }

    public void saveList(MediaList mediaList) {
        localDatabaseHandle.saveList(mediaList);
    }

    public ArrayList<MediaList> getLists(User user) {
        return localDatabaseHandle.getLists(user);
    }

    public void deleteList(MediaList mediaList) { localDatabaseHandle.deleteList(mediaList);}
}
