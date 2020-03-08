package com.cs371.storage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionManager implements StorageManager {

    DatabaseConnectionManager instance = new DatabaseConnectionManager();
    ArrayList<DatabaseHandle> databaseConnections = new ArrayList<DatabaseHandle>();

    public DatabaseConnectionManager DatabaseConnectionManager(){
        if(instance == null){
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    private void createDatabaseConnection(){

    }

    private void connectDatabases(){
        for(DatabaseHandle databaseConnection : databaseConnections ){
            databaseConnection.connect();
        }
    }

    @Override
    public List<StorageHandle> getStorageHandles() {
        return databaseConnections;
    }
}
