package com.cs371.storage;

interface DatabaseHandle {
    void connect();

    String search(DataType dataType, String data);
}
