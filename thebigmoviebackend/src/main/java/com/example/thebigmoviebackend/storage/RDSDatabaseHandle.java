package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

class RDSDatabaseHandle extends LocalDatabaseHandle {
    Connection connection = null;

    @Override
    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String dbName = "cs371";
                String userName = "admin";
                String password = "cs371sunypoly";
                String hostname = "cs371-1.cnelkifhz6ce.us-east-2.rds.amazonaws.com";
                String port = "3306";
                String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
                System.out.println(jdbcUrl);
                System.out.println("Getting remote connection with connection string from environment variables.");
                Connection con = DriverManager.getConnection(jdbcUrl);
                System.out.println("Remote connection successful.");
                connection = con;
            } catch (ClassNotFoundException e) {
                System.out.println(e.toString());
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
        return connection;
    }

    @Override
    public void prepare() {

    }

    @Override
    public void save(DataType dataType, String data) {

    }

    @Override
    public ArrayList<Movie> search(DataType dataType, String data) {
        switch(dataType){
            case USER:
                break;
            case LIST:
                break;
            case MOVIE:
                break;
            case ACTOR:
                break;
        }
        return null;
    }

    public User getUser(String data) {
        return null;
    }

    @Override
    public void saveMovies(ArrayList<Movie> data) {
        Connection connection = connect();
        for(Movie movie : data) {
            String query = " insert into MOVIES (title, voteAverage, voteCount, video, posterPath,remoteId,adult,backgroundPath,originalLanguage,originalTitle,genreIds,overview,releaseDate)"
                    + " values (?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?)";

            // create the mysql insert preparedstatement
            try {
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setString(1, movie.getTitle());
                preparedStmt.setDouble(2, movie.getVoteAverage());
                preparedStmt.setInt(3, movie.getVoteCount());
                preparedStmt.setBoolean(4, movie.getVideo());
                preparedStmt.setString(5, movie.getPosterPath());

                preparedStmt.setInt(6, movie.getID());
                preparedStmt.setBoolean(7, movie.getAdult());
                preparedStmt.setString(8, movie.getBackgroundPath());
                preparedStmt.setString(9, movie.getOriginalLanguage());
                preparedStmt.setInt(10, 1);
                preparedStmt.setString(11, movie.getOverview());
                preparedStmt.setString(10, movie.getReleaseDate());

                // execute the preparedstatement
                preparedStmt.execute();

                connection.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public void saveUser(User data) {

    }
}
