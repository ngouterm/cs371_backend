package com.example.thebigmoviebackend.storage;

import com.example.thebigmoviebackend.model.MediaList;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

final class RDSDatabaseHandle implements ApplicationDatabaseHandle {
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
    public ArrayList<?> search(DataType dataType, String data) {
        ArrayList<Object> retVal = new ArrayList<>();
        switch (dataType) {
            case USER:
                try {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE username LIKE ?");
                    statement.setString(1, "%" + data + "%");
                    ResultSet resultSet = statement.executeQuery();
                    statement.clearParameters();

                    while (resultSet.next()) {
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String userUUID = resultSet.getString("userUUID");
                        User user = new User(username, userUUID);
                        retVal.add(user);
                    }
                    return retVal;
                } catch (Exception e) {
                    System.err.println("Got an exception! ");
                    System.err.println(e.getMessage());
                }
                break;

            case LIST:
//                try {
//                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM MEDIA WHERE title LIKE ?");
//                    statement.setString(1, "%" + data + "%");
//                    ResultSet resultSet = statement.executeQuery();
//                    statement.clearParameters();
//
//                    while (resultSet.next()) {
//                        String uuid = resultSet.getString("mediaListUUID");
//                        String title = resultSet.getString("listTitle");
//                        MediaList mediaList = new MediaList(user, title, uuid);
//                    }
//                    return retVal;
//                } catch (Exception e) {
//                    System.err.println("Got an exception! ");
//                    System.err.println(e.getMessage());
//                }
                break;
            case MOVIE: {
                try {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM MEDIA WHERE title LIKE ?");
                    statement.setString(1, "%" + data + "%");
                    ResultSet resultSet = statement.executeQuery();
                    statement.clearParameters();

                    while (resultSet.next()) {
                        Movie movie = new Movie(resultSet.getString("title"));
                        movie.setUuid(resultSet.getString("mediaUUID"));
                        retVal.add(movie);
                    }
                    return retVal;
                } catch (Exception e) {
                    System.err.println("Got an exception! ");
                    System.err.println(e.getMessage());
                }
                break;
            }
        }
        return null;
    }

    @Override
    public User getUserByUsername(String data) {

        String userName = "";
        String password;
        String uuid = "";
        try {
            Connection conn = connect();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE username = ?");
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
    public MediaList getMediaListByUUID(String uuid) {
        MediaList mediaList = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM MEDIALIST_LOOKUP mll JOIN USERS u ON mll.idUser = u.idUser JOIN MEDIALIST ml ON mll.mediaListUUID = ml.mediaListUUID WHERE mll.mediaListUUID = ?");
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            String title;
            while (resultSet.next()) {
                User user = getUserByUUID(resultSet.getString("userUUID"));
                title = resultSet.getString("listTitle");
                mediaList = new MediaList(user, title, uuid);

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String query = "SELECT * FROM MEDIALIST ml JOIN MEDIA m ON ml.idMedia = m.idMedia WHERE ml.mediaListUUID = " + "'" + mediaList.getUUID() + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String mediaTitle;
                String mediaUUID;
                String releaseDate;
                mediaTitle = resultSet.getString("title");
                mediaUUID = resultSet.getString("mediaUUID");
                releaseDate = resultSet.getString("releaseDate");
                Movie movie = new Movie(mediaTitle);
                movie.setUuid(mediaUUID);
                movie.setReleaseDate(releaseDate);
                mediaList.addMovie(movie);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mediaList;
    }



    @Override
    public Movie getMovieByUUID(String uuid) {
        Movie retVal = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM MEDIA WHERE mediaUUID = ?");
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();

            while (resultSet.next()) {
                Movie movie = new Movie(resultSet.getString("title"));
                movie.setUuid(resultSet.getString("mediaUUID"));
                retVal = movie;
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return retVal;
    }

    @Override
    public User getUserByUUID(String uuid) {

        String userName = "";
        String password;

        try {
            Connection conn = connect();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE userUUID = ?");
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();

            while (resultSet.next()) {
                userName = resultSet.getString("username");
                password = resultSet.getString("password");
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
    public void saveMovies(List<Movie> data) {
        Connection connection = connect();
        for (Movie movie : data) {
            String query = "INSERT INTO MEDIA (title, voteAverage, voteCount, video, posterPath,remoteId,adult,backgroundPath,originalLanguage,originalTitle,genreIds,overview,releaseDate, mediaUUID)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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

        String query = "INSERT INTO USERS (username, password, userUUID)"
                + " VALUES (?, ?,?)";

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
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE userUUID = ?");
                statement.setString(1, mediaList.getUser().getUuid());
                ResultSet resultSet = statement.executeQuery();
                statement.clearParameters();

                while (resultSet.next()) {
                    id = resultSet.getInt("idUser");
                }

                String query = "INSERT INTO MEDIALIST_LOOKUP (idUser,listTitle,  mediaListUUID)"
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
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM MEDIA WHERE mediaUUID = ?");
                    statement.setString(1, movie.getUuid());
                    ResultSet resultSet = statement.executeQuery();
                    statement.clearParameters();

                    while (resultSet.next()) {
                        id = resultSet.getInt("idMedia");
                    }

                    String query = "INSERT INTO MEDIALIST (idMedia, mediaListUUID)"
                            + " VALUES (?, ?)";
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
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE userUUID = ?");
                statement.setString(1, mediaList.getUser().getUuid());
                ResultSet resultSet = statement.executeQuery();
                statement.clearParameters();

                while (resultSet.next()) {
                    id = resultSet.getInt("idUser");
                }

                String query = "INSERT INTO MEDIALIST_LOOKUP (idUser, listTitle, mediaListUUID)"
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
    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS ");
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String userUUID = resultSet.getString("userUUID");
                User user = new User(username, userUUID);
                userList.add(user);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }


    @Override
    public ArrayList<MediaList> getAllLists() {
        ArrayList<MediaList> mediaLists = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>(getAllUsers());
        for (User user : users) {
            mediaLists.addAll(getLists(user));
        }
        return mediaLists;
    }

    @Override
    public ArrayList<Movie> getAllMedia() {
        ArrayList<Movie> allMedia = new ArrayList<>();
        try {
            String query = "SELECT * FROM MEDIA ml";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String mediaTitle;
                String mediaUUID;
                String releaseDate;
                mediaTitle = resultSet.getString("title");
                mediaUUID = resultSet.getString("mediaUUID");
                releaseDate = resultSet.getString("releaseDate");
                Movie movie = new Movie(mediaTitle);
                movie.setUuid(mediaUUID);
                movie.setReleaseDate(releaseDate);
                allMedia.add(movie);
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allMedia;
    }

    @Override
    public ArrayList<MediaList> getLists(User user) {
        ArrayList<MediaList> mediaListArrayList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM MEDIALIST_LOOKUP mll JOIN USERS u ON mll.idUser = u.idUser JOIN MEDIALIST ml ON mll.mediaListUUID = ml.mediaListUUID WHERE u.userUUID = ?");
            statement.setString(1, user.getUuid());
            ResultSet resultSet = statement.executeQuery();
            statement.clearParameters();
            String uuid;
            String title;
            while (resultSet.next()) {
                uuid = resultSet.getString("mediaListUUID");
                title = resultSet.getString("listTitle");
                MediaList mediaList = new MediaList(user, title, uuid);
                mediaListArrayList.add(mediaList);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            for (MediaList mediaList : mediaListArrayList) {
                String query = "SELECT * FROM MEDIALIST ml JOIN MEDIA m ON ml.idMedia = m.idMedia WHERE ml.mediaListUUID = " + "'" + mediaList.getUUID() + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String mediaTitle;
                    String mediaUUID;
                    String releaseDate;
                    mediaTitle = resultSet.getString("title");
                    mediaUUID = resultSet.getString("mediaUUID");
                    releaseDate = resultSet.getString("releaseDate");
                    Movie movie = new Movie(mediaTitle);
                    movie.setUuid(mediaUUID);
                    movie.setReleaseDate(releaseDate);
                    mediaList.addMovie(movie);
                }
                resultSet.close();
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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM MEDIALIST ml WHERE ml.mediaListUUID = ?");
            statement.setString(1, mediaList.getUUID());
            ResultSet resultSetMedia = statement.executeQuery();
            statement.clearParameters();
            while (resultSetMedia.next()) {
                int listId = resultSetMedia.getInt("idMedialist");
                PreparedStatement statementMLD = connection.prepareStatement("DELETE FROM MEDIALIST ml WHERE ml.mediaListUUID = ?");
                statementMLD.setString(1, mediaList.getUUID());
                statementMLD.execute();
                PreparedStatement statementMLLD = connection.prepareStatement("DELETE FROM MEDIALIST_LOOKUP WHERE idMedialist = ?");
                statementMLLD.setInt(1, listId);
                statementMLLD.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
