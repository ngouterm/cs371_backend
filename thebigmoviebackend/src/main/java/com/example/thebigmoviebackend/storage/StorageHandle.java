package com.example.thebigmoviebackend.storage;

interface StorageHandle {
    void prepare();

    void save(DataType dataType, String data);
}
