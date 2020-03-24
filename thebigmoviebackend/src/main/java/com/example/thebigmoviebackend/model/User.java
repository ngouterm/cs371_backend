package com.example.thebigmoviebackend.model;

import java.util.Objects;
import java.util.UUID;

public class User {
    String username;
    String password = "";
    String email = "";
    String uuid;

    public User(String username) {
        this.username = username;
        this.uuid = UUID.randomUUID().toString();
    }

    public User(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUuid(){ return uuid;}

    @Override
    public String toString() {
        return username;
    }
}
