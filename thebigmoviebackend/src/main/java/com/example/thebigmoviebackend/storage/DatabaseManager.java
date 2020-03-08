package com.example.thebigmoviebackend.storage;

import java.util.ArrayList;

public class DatabaseManager implements StorageManager {

    DatabaseManager instance = new DatabaseManager();
    ArrayList<LocalDatabaseHandle> localDatabaseHandles = new ArrayList<LocalDatabaseHandle>();
    ArrayList<RemoteDatabaseHandle> remoteDatabaseHandles = new ArrayList<RemoteDatabaseHandle>();

    private void DatabaseConnectionManager(){
        connectDatabases();
    }

    public DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
            connectDatabases();
        }
        return instance;
    }

    private void createDatabaseConnection(){

    }

    private void connectDatabases(){
        for(LocalDatabaseHandle databaseHandle : localDatabaseHandles ){
            databaseHandle.connect();
        }
        for(RemoteDatabaseHandle databaseHandle : remoteDatabaseHandles){
            databaseHandle.connect();
        }
    }

    @Override
    public ArrayList<LocalDatabaseHandle> getStorageHandles() {
        return localDatabaseHandles;
    }

    public String query(DataType dataType, DataAction dataAction, String data){
        if(dataAction == DataAction.SAVE){
            for(LocalDatabaseHandle localDatabaseHandle: localDatabaseHandles){
                localDatabaseHandle.save(dataType, data);
            }
        }else if(dataAction == DataAction.SEARCH){
            switch(dataType){
                case USER:{
                    for(LocalDatabaseHandle localDatabaseHandle: localDatabaseHandles){
                        localDatabaseHandle.search(dataType, data);
                    }
                }
                default:
                    for(RemoteDatabaseHandle remoteDatabaseHandle : remoteDatabaseHandles){
                        remoteDatabaseHandle.search(dataType, data);
                    }
            }
        }
        return null;
    }


}
