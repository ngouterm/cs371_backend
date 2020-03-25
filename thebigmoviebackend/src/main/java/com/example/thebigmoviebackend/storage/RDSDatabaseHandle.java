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
                String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password + "&useSSL=false";
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

                    PreparedStatement statement = connection.prepareStatement("select * from MEDIA where title LIKE ?");
                    statement.setString(1, "%" + data + "%");
                    ResultSet resultSet = statement.executeQuery();
                    statement.clearParameters();

                    while (resultSet.next()) {
                        Movie movie = new Movie(resultSet.getString("title"));
                        movie.setUuid(resultSet.getString("mediaUUID"));
                        moviesToReturn.add(movie);
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

            PreparedStatement statement = connection.prepareStatement("select * from USERS where username = ?");
            statement.setString(1, data);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();

            while (resultSet.next()) {
                userName = resultSet.getString("username");
                password = resultSet.getString("password");
                uuid = resultSet.getString("userUUID");
            }
            if (!userName.equals("") && !uuid.equals("")) {
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

        String query = " insert into USERS (username, password, userUUID)"
                + " values (?, ?,?)";

        // create the mysql insert preparedstatement
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, data.getUsername());
            preparedStmt.setString(2, data.getPassword());
            preparedStmt.setString(3, data.getUuid());

            // execute the preparedstatement
            preparedStmt.execute();

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public void saveList(MediaList mediaList) {
        saveMovies(mediaList.getMovies());
        Connection connection = connect();
        if (mediaList.getMovies().isEmpty()) {
            int id = -1;
            try {
                PreparedStatement statement = connection.prepareStatement("select * from USERS where userUUID = ?");
                statement.setString(1, mediaList.getUser().getUuid());
                ResultSet resultSet = statement.executeQuery();
                statement.clearParameters();

                while (resultSet.next()) {
                    id = resultSet.getInt("idUser");
                }

                String query = " insert into MEDIALIST_LOOKUP (idUser,listTitle,  mediaListUUID)"
                        + " values (?,?,?)";

                PreparedStatement preparedStmt = connection.prepareStatement(query);
                if (id == -1) {
                    throw new SQLException();
                }
                preparedStmt.setInt(1, id);
                preparedStmt.setString(2, mediaList.getName());
                preparedStmt.setString(3, mediaList.getUUID());

                // execute the preparedstatement
                preparedStmt.execute();

            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        } else {

            for (Movie movie : mediaList.getMovies()) {
                int id = -1;
                try {
                    PreparedStatement statement = connection.prepareStatement("select * from MEDIA where mediaUUID = ?");
                    statement.setString(1, movie.getUuid());
                    ResultSet resultSet = statement.executeQuery();
                    statement.clearParameters();

                    while (resultSet.next()) {
                        id = resultSet.getInt("idMedia");
                    }

                    String query = "insert into MEDIALIST (idMedia, mediaListUUID)"
                            + " values (?, ?)";
                    try {
                        PreparedStatement preparedStmt = connection.prepareStatement(query);
                        if (id == -1) {
                            throw new SQLException();
                        }
                        preparedStmt.setInt(1, id);
                        preparedStmt.setString(2, mediaList.getUUID());

                        // execute the preparedstatement
                        preparedStmt.execute();
                    } catch (SQLException e) {
                        System.out.println(e.toString());
                    }


                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }

            // create the mysql insert preparedstatement
            int id = -1;
            try {
                PreparedStatement statement = connection.prepareStatement("select * from USERS where userUUID = ?");
                statement.setString(1, mediaList.getUser().getUuid());
                ResultSet resultSet = statement.executeQuery();
                statement.clearParameters();

                while (resultSet.next()) {
                    id = resultSet.getInt("idUser");
                }

                String query = " insert into MEDIALIST_LOOKUP (idUser, listTitle, mediaListUUID)"
                        + " values (?,?,?)";

                PreparedStatement preparedStmt = connection.prepareStatement(query);
                if (id == -1) {
                    throw new SQLException();
                }
                preparedStmt.setInt(1, id);
                preparedStmt.setString(2, mediaList.getName());
                preparedStmt.setString(3, mediaList.getUUID());

                // execute the preparedstatement
                preparedStmt.execute();

            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public ArrayList<MediaList> getLists(User user) {
        ArrayList<MediaList> mediaListArrayList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from MEDIALIST_LOOKUP mll join USERS u ON mll.idUser = u.idUser JOIN MEDIALIST ml ON mll.mediaListUUID = ml.mediaListUUID WHERE u.userUUID = ?");
            statement.setString(1, user.getUuid());
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            String uuid;
            String title;
            while (resultSet.next()) {

                uuid = resultSet.getString("mediaListUUID");
                title = resultSet.getString("listTitle");

                MediaList mediaList = new MediaList(user, uuid);
                PreparedStatement statementMedia = connection.prepareStatement("select * from MEDIALIST ml JOIN MEDIA m on ml.idMedia = m.idMedia WHERE ml.mediaListUUID = ?");
                statementMedia.setString(1, mediaList.getUUID());
                ResultSet resultSetMedia = statementMedia.executeQuery();
                statementMedia.clearParameters();
                String mediaTitle;
                String mediaUUID;
                while (resultSetMedia.next()) {
                    mediaTitle = resultSetMedia.getString("title");
                    mediaUUID = resultSetMedia.getString("mediaUUID");
                    Movie movie = new Movie(mediaTitle);
                    movie.setUuid(mediaUUID);
                    mediaList.addMovie(movie);
                }
                mediaList.setName(title);
                mediaListArrayList.add(mediaList);
            }
            return mediaListArrayList;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;

    }

    @Override
    public void deleteList(MediaList mediaList) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from MEDIALIST ml WHERE ml.mediaListUUID = ?");
            statement.setString(1, mediaList.getUUID());
            ResultSet resultSetMedia = statement.executeQuery();
            statement.clearParameters();
            while (resultSetMedia.next()) {
                int listId = resultSetMedia.getInt("idMedialist");
                PreparedStatement statementMLD = connection.prepareStatement("DELETE from MEDIALIST ml WHERE ml.mediaListUUID = ?");
                statementMLD.setString(1, mediaList.getUUID());
                statementMLD.execute();
                PreparedStatement statementMLLD = connection.prepareStatement("DELETE from MEDIALIST_LOOKUP WHERE idMedialist = ?");
                statementMLLD.setInt(1, listId);
                statementMLLD.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
