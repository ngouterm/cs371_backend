package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.sql.*;
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
        ArrayList<Movie> moviesToReturn = new ArrayList<>();
        switch (dataType) {
            case USER:
                break;
            case LIST:
                break;
            case MOVIE: {
                try {
                    Connection conn = connect();

                    PreparedStatement statement = connection.prepareStatement("select * from MEDIA where title = ?");
                    statement.setString(1, data);
                    ResultSet resultSet = statement.executeQuery();
                    statement.clearParameters();

                    while (resultSet.next()) {
                        resultSet.getString("title");
                        moviesToReturn.add(new Movie(resultSet.getString("title")));
                    }
                    return moviesToReturn;
                } catch (Exception e) {
                    System.err.println("Got an exception! ");
                    System.err.println(e.getMessage());
                }
                break;
            }
            case ACTOR:
                break;
        }
        return null;
    }

    public User getUser(String data) {

        String userName = "";
        String password;
        String uuid = "";
        try {
            Connection conn = connect();

            PreparedStatement statement = connection.prepareStatement("select * from USER where username = ?");
            statement.setString(1, data);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();

            while (resultSet.next()) {
                userName = resultSet.getString("username");
                password = resultSet.getString("password");
                uuid = resultSet.getString("userUUID");
            }
            if(!userName.equals("") && !uuid.equals("")) {
                return new User(userName, uuid);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void saveMovies(ArrayList<Movie> data) {
        Connection connection = connect();
        for (Movie movie : data) {
            String query = " insert into MEDIA (title, voteAverage, voteCount, video, posterPath,remoteId,adult,backgroundPath,originalLanguage,originalTitle,genreIds,overview,releaseDate, mediaUUID)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
                preparedStmt.setString(10, movie.getOriginalTitle());
                preparedStmt.setInt(11, 1);
                preparedStmt.setString(12, movie.getOverview());
                preparedStmt.setString(13, movie.getReleaseDate());
                preparedStmt.setString(14, movie.getUuid());

                // execute the preparedstatement
                preparedStmt.execute();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public void saveUser(User data) {
        Connection connection = connect();

        String query = " insert into USER (username, password, userUUID)"
                + " values (?, ?,?)";

        // create the mysql insert preparedstatement
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, data.getUsername());
            preparedStmt.setString(2, data.getPassword());
            preparedStmt.setString(2, data.getUuid());

            // execute the preparedstatement
            preparedStmt.execute();

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }
    @Override
    public void saveList(MediaList mediaList) {
        Connection connection = connect();

        for (Movie movie : mediaList.getMovies()) {
            int id = -1;
            try {
                PreparedStatement statement = connection.prepareStatement("select * from MEDIA where title = ?");
                statement.setString(1, movie.getTitle());
                ResultSet resultSet = statement.executeQuery();
                statement.clearParameters();

                while (resultSet.next()) {
                    id = resultSet.getInt("idMedia");
                }
                String query = " insert into MEDIALIST (idMedia, mediaListUUID)"
                        + " values (?, ?)";

                // create the mysql insert preparedstatement

                try {
                    PreparedStatement preparedStmt = connection.prepareStatement(query);
                    if(id == -1 ){
                        throw new SQLException();
                    }
                    preparedStmt.setInt(1, id);
                    preparedStmt.setString(2, mediaList.getUUID());

                    // execute the preparedstatement
                    preparedStmt.execute();

                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }

            }catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

        // create the mysql insert preparedstatement
        int id = -1;
        try {
            PreparedStatement statement = connection.prepareStatement("select * from USER where userUUID = ?");
            statement.setString(1, mediaList.getUser().getUuid());
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();

            while (resultSet.next()) {
                id = resultSet.getInt("idUser");
            }

            String query = " insert into MEDIALIST (idUser)"
                    + " values (?)";

            PreparedStatement preparedStmt = connection.prepareStatement(query);
            if(id == -1){
                throw new SQLException();
            }
            preparedStmt.setInt(1, id);

            // execute the preparedstatement
            preparedStmt.execute();

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
}
