//package com.example.thebigmoviebackend.command;
//
//import com.example.thebigmoviebackend.model.MediaList;
//import com.example.thebigmoviebackend.model.Movie;
//import com.example.thebigmoviebackend.model.User;
//import com.example.thebigmoviebackend.service.DatabaseService;
//import com.example.thebigmoviebackend.service.MixedDatabaseService;
//import com.example.thebigmoviebackend.service.UserService;
//import org.jline.reader.LineReader;
//import org.jline.terminal.Terminal;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import org.springframework.shell.standard.ShellOption;
//import org.springframework.shell.table.ArrayTableModel;
//import org.springframework.shell.table.BorderStyle;
//import org.springframework.shell.table.TableBuilder;
//import org.springframework.shell.table.TableModel;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@ShellComponent
//public class DatabaseCommands {
//
//    MixedDatabaseService databaseService = new MixedDatabaseService();
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    Terminal terminal;
//
//    @Autowired
//    LineReader lineReader;
//
//    User currentUser = null;
//    ArrayList<Movie> recentResults = null;
//    MediaList recentMediaList = null;
//
//    private final String allDatabases = "all";
//
//    int shellWidth = 80;
//
//    @ShellMethod("List all databases")
//    public String listDbs() {
//        return tablify(databaseService.getExternalDatabases());
//    }
//
//    ArrayList<DatabaseService> databaseServices(String databases) {
//        if (databases.equals(allDatabases)) {
//            return new ArrayList<>(databaseService.getDatabases().values());
//        }
//
//        ArrayList<DatabaseService> services = new ArrayList<>();
//        String[] dbs = databases.split(",");
//        for (String db : dbs) {
//            DatabaseService service = databaseService.getDatabases().get(db);
//            if (service != null) {
//                services.add(service);
//            }
//        }
//
//        return services;
//    }
//
//    @ShellMethod("Search for a movie, television show, or actor.")
//    public String search(String query, @ShellOption(value = {"-db"}, defaultValue = allDatabases) String database) {
//        String message = "You searched for '" + query + "'\n\n";
//
//        HashMap<DatabaseService, ArrayList<Movie>> results = databaseService.getMovieResults(query, databaseServices(database));
//        recentResults = (ArrayList<Movie>) results.values().stream().flatMap(List::stream).collect(Collectors.toList());
//
//        if (results.keySet().isEmpty()) {
//            return "Could not find databases based on '" + database + "'.";
//        }
//        if (recentResults.isEmpty()) {
//            return "No results found";
//        }
//
//        int startIndex = 1;
//        for (DatabaseService dbs : results.keySet()) {
//            if (!results.isEmpty()) {
//                message = message.concat(tablify(dbs.toString(), results.get(dbs), true, startIndex));
//                startIndex += results.get(dbs).size();
//            } else {
//                message = message.concat("No results found.");
//            }
//        }
//
//        return message;
//    }
//
//    @ShellMethod("Search for a user")
//    public String searchUsers(String query) {
//        ArrayList<User> results = userService.searchUsers(query);
//        if (results.isEmpty()) {
//            return "Found no results.";
//        }
//        return tablify("Found the following users: ", userService.searchUsers(query));
//    }
//
//    public <T> String tablify(List<T> list) {
//        return tablify(null, list, false, 1);
//    }
//
//    public <T> String tablify(String title, List<T> list) {
//        return tablify(title, list, false, 1);
//    }
//
//
//    public <T> String tablify(String title, List<T> list, boolean includeIndices, int startIndex) {
//        int offset = title == null ? 0 : 1;
//
//        Object[][] table = new Object[list.size() + offset][includeIndices ? 2 : 1];
//        if (title != null) {
//            table[0][0] = title;
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (includeIndices) {
//                table[i + offset][0] = i + startIndex;
//                table[i + offset][1] = list.get(i);
//            } else {
//                table[i + offset][0] = list.get(i);
//            }
//        }
//
//        TableModel tableModel = new ArrayTableModel(table);
//        TableBuilder tableBuilder = new TableBuilder(tableModel);
//        tableBuilder.addFullBorder(BorderStyle.fancy_light);
//        return tableBuilder.build().render(shellWidth);
//    }
//
//    @ShellMethod("Create a user")
//    public void createUser(@ShellOption(defaultValue = "NONE") String username) {
//        if (username.equals("NONE")) {
//            username = lineReader.readLine("Enter username (or q to exit): ");
//            if (username.equals("q")) {
//                return;
//            }
//        }
//        boolean createdUser = userService.createUser(username);
//        while (!createdUser) {
//            terminal.writer().println("User '" + username + "' already exists or is an invalid name.");
//            username = lineReader.readLine("Enter username (or q to exit): ");
//            if (username.equals("q")) {
//                return;
//            }
//            createdUser = userService.createUser(username);
//        }
//        terminal.writer().println("Created user '" + username + "'.");
//        terminal.flush();
//    }
//
//    @ShellMethod("Login")
//    public String login(String username) {
//        //TODO: password verification
//        currentUser = userService.login(username, null);
//        if (currentUser == null) {
//            return "Could not find user '" + username + "'.";
//        } else {
//            return "Logged in as '" + username + "'";
//        }
//    }
//
//
//    @ShellMethod("Explicitly save a movie to the local database (not recommended; for debug purposes)")
//    public String saveRaw(String movieName) {
//        Movie movie = new Movie(movieName);
//        ArrayList<Movie> movies = new ArrayList<>();
//        movies.add(movie);
//        databaseService.saveMovies(movies);
//        return "Saved movie";
//    }
//
//    interface RequireLogin {
//        String action();
//    }
//
//    public String requireLogin(RequireLogin requireLogin) {
//        if (currentUser == null) {
//            return "Must be logged in to perform that action";
//        } else {
//            return requireLogin.action();
//        }
//    }
//
//    @ShellMethod("List all lists for a user")
//    public String listLists(@ShellOption(defaultValue = "NONE") String username) {
//        User user = getUser(username);
//        ArrayList<MediaList> mediaLists = userService.getMediaLists(user);
//
//        return tablify("Lists by " + user, mediaLists);
//    }
//
//    @ShellMethod("List all movies in internal database")
//    public String listMovies() {
//        ArrayList<Movie> movies = databaseService.getAllMovies();
//        if (movies.isEmpty()) {
//            return "No movies.";
//        }
//        return tablify("Found these movies.", movies);
//    }
//
//    @ShellMethod("List all lists")
//    public String listAll() {
//        ArrayList<MediaList> lists = databaseService.getAllLists();
//        if (lists.isEmpty()) {
//            return "No lists.";
//        }
//        return tablify("Found these lists.", lists);
//    }
//
//    @ShellMethod("Make list")
//    public String makeList(String name) {
//        return requireLogin(() -> {
//            recentMediaList = userService.createMediaList(currentUser, name);
//            return "Created list";
//        });
//    }
//
//    @ShellMethod("Display a list")
//    public void listList(@ShellOption(defaultValue = "NONE") String name, @ShellOption(defaultValue = "NONE") String username) {
//        User user;
//        user = getUser(username);
//        MediaList mediaList = getMediaList(user, name);
//        System.out.println(mediaList.getName() + " by " + mediaList.getUser());
//        System.out.println("=================================================");
//        for (Movie movie : mediaList.getMovies()) {
//            System.out.println(movie);
//        }
//    }
//
//    @ShellMethod("Get all users")
//    public String listUsers() {
//        ArrayList<User> users = userService.getAllUsers();
//        if (users.isEmpty()) {
//            return "Found no users.";
//        }
//        return tablify("Users", users);
//    }
//
//    @ShellMethod("Log out")
//    public String logout() {
//        currentUser = null;
//        return "Logged out";
//    }
//
//    private User getUser(@ShellOption(defaultValue = "NONE") String username) {
//        if (username.equals("NONE")) {
//            return currentUser;
//        } else {
//            //TODO: password verification
//            return userService.login(username, null);
//        }
//    }
//
//    private MediaList getMediaList(User user, @ShellOption(defaultValue = "NONE") String listName) {
//        if (!listName.equals("NONE")) {
//            recentMediaList = userService.getMediaList(user, listName);
//        }
//        return recentMediaList;
//    }
//
//    @ShellMethod("Save an item from recent results")
//    public String save(int index, @ShellOption(defaultValue = "NONE") String listName) {
//        return requireLogin(() -> {
//            try {
//                Movie movie = recentResults.get(index - 1);
//                MediaList mediaList = getMediaList(currentUser, listName);
//                mediaList.addMovie(movie);
//                userService.deleteMediaList(currentUser, mediaList);
//                userService.saveMediaList(mediaList);
//                return "Saved item to list.";
//            } catch (NullPointerException e) {
//                return e.getLocalizedMessage();
//            }
//        });
//    }
//
//    @ShellMethod("Remove item from list")
//    public String remove(int index, @ShellOption(defaultValue = "NONE") String listName) {
//        return requireLogin(() -> {
//            MediaList mediaList = getMediaList(currentUser, listName);
//            Movie movie = mediaList.removeMovie(index - 1);
//            userService.saveMediaList(mediaList);
//            return "Removed '" + movie + "'.";
//        });
//    }
//
//    @ShellMethod("Delete list")
//    public String deleteList(@ShellOption(defaultValue = "NONE") String listName) {
//        return requireLogin(() -> {
//            MediaList mediaList = getMediaList(currentUser, listName);
//            userService.deleteMediaList(currentUser, mediaList);
//            return "Deleted list.";
//        });
//    }
//
//    @ShellMethod("Echo locally stored values")
//    public String echo(String value) {
//        switch (value) {
//            case "user":
//                return currentUser.toString();
//            case "search":
//                return tablify("Recent results", recentResults, true, 1);
//            case "list":
//                return tablify(recentMediaList.getName(), recentMediaList.getMovies());
//            default:
//                int i = Integer.parseInt(value);
//                return recentResults.get(i - 1).toString();
//        }
//    }
//}
