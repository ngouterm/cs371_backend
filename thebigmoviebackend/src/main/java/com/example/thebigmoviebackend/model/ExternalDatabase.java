package com.example.thebigmoviebackend.model;

public class ExternalDatabase {
    private final String name;

    public ExternalDatabase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static final ExternalDatabase INVALID_DATABASE = new ExternalDatabase(null);

    @Override
    public String toString() {
        return name == null? "Invalid database": name;
    }
}

