package com.cs371.storage;

interface StorageHandle {
    void prepare();

    void save(DataType dataType, String data);
}
