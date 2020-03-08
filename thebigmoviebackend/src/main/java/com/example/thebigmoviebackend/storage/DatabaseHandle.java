package com.example.thebigmoviebackend.storage;

interface DatabaseHandle {
    void connect();

    String search(DataType dataType, String data);
}
