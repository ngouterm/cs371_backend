package com.example.thebigmoviebackend.model;

import java.util.ArrayList;
import java.util.UUID;

public class Comment {
    public String getUserUUID() {
        return userUUID;
    }

    public String getComment() {
        return comment;
    }

    public String getMovieUUID() {
        return movieUUID;
    }

    public String getUuid() {
        return uuid;
    }

    String userUUID;
    String comment;
    String movieUUID;
    String uuid;

    /**
     * @param user User who made the comment
     * @param movie Movie commented on
     * @param comment contents of comment
     * This comment will generate a new random uuid
     */
    public Comment(User user,Movie movie, String comment) {
        this(user, movie, comment, UUID.randomUUID().toString());
    }

    /**
     * @param userUUID UUID of User who made the comment
     * @param movieUUID UUID of Movie commented on
     * @param comment contents of comment
     * This comment will generate a new random uuid
     */
    public Comment(String userUUID,String movieUUID, String comment) {
        this(userUUID, movieUUID, comment, UUID.randomUUID().toString());
    }

    /**
     /**
     * @param user User who made the comment
     * @param movie Movie commented on
     * @param comment contents of comment
     * @param uuid UUID of comment used to create an object of an existing comment
     */
    public Comment(User user,Movie movie, String comment, String uuid) {
        this(user.getUuid(), movie.getUuid(), comment, uuid);
    }

    /**
     * @param userUUID UUID of User who made the comment
     * @param movieUUID UUID of Movie commented on
     * @param comment comment to save
     * @param uuid uuid of comment
     */
    public Comment(String userUUID,String movieUUID, String comment, String uuid) {
        this.userUUID = userUUID;
        this.movieUUID = movieUUID;
        this.comment = comment;
        this.uuid = uuid;
    }


}
