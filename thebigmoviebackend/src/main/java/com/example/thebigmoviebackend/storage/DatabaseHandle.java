package com.example.thebigmoviebackend.storage;

import java.sql.Connection;
import java.util.Collection;

interface DatabaseHandle {
    Connection connect();

    Collection<?> search(DataType dataType, String data);
}
