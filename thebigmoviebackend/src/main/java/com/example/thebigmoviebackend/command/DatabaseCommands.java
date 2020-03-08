package com.example.thebigmoviebackend.command;

import com.example.thebigmoviebackend.model.ExternalDatabase;
import com.example.thebigmoviebackend.model.Movie;
import com.example.thebigmoviebackend.service.DatabaseService;
import com.example.thebigmoviebackend.service.UserService;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ShellComponent
public class DatabaseCommands {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    UserService userService;

    @Autowired
    Terminal terminal;

    @Autowired
    LineReader lineReader;

    private final String allDatabases = "all";

    @ShellMethod("Search for a movie, television show, or actor.")
    public String search(String query, @ShellOption(value = {"-db"}, defaultValue = allDatabases) String database) {
        int shellWidth = 80;

        String message = "You searched for '" + query + "'\n\n";
        message = message.concat("We searched the following databases:\n");

        ExternalDatabaseResolver externalDatabaseResolver = parseDatabase(database);
        Object[][] dbTable = tablify("Databases", externalDatabaseResolver.externalDatabases);
        TableModel dbTableModel = new ArrayTableModel(dbTable);
        TableBuilder dbTableBuilder = new TableBuilder(dbTableModel);
        dbTableBuilder.addFullBorder(BorderStyle.fancy_light);
        message = message.concat(dbTableBuilder.build().render(shellWidth) + "\n");

        ArrayList<Movie> results = DatabaseManager.getMovieResults(query, externalDatabaseResolver.getExternalDatabases());
        if (!results.isEmpty()) {
            message = message.concat("We got the following results:\n");
            Object[][] resultsTable = tablify(null, databaseService.getMovieResults(query, externalDatabaseResolver.getExternalDatabases()));
            TableModel resultsTableModel = new ArrayTableModel(resultsTable);
            message = message.concat(resultsTableModel.getRowCount() + "\n");
            TableBuilder resultsTableBuilder = new TableBuilder(resultsTableModel);
            resultsTableBuilder.addFullBorder(BorderStyle.fancy_light);
            message = message.concat(resultsTableBuilder.build().render(shellWidth));
        } else {
            message = message.concat("No results found.");
        }

        return message;
    }

    public ExternalDatabaseResolver parseDatabase(String database) {
        if (database.equals(allDatabases)) {
            return new ExternalDatabaseResolver(databaseService.getAvailableExternalDatabases(), null);
        } else {
            HashSet<ExternalDatabase> externalDatabases = new HashSet<>();
            ArrayList<String> errorNames = new ArrayList<>();
            String[] dbs = database.split(",");
            for (String db : dbs) {
                boolean foundCorrespondingDb = false;
                for (ExternalDatabase externalDatabase : databaseService.getAvailableExternalDatabases()) {
                    if (externalDatabase.getName().toLowerCase().equals(db.toLowerCase())) {
                        externalDatabases.add(externalDatabase);
                        foundCorrespondingDb = true;
                    }
                }
                if (!foundCorrespondingDb) {
                    errorNames.add(db);
                    externalDatabases.add(ExternalDatabase.INVALID_DATABASE);
                }
            }
            return new ExternalDatabaseResolver(new ArrayList<>(externalDatabases), errorNames);
        }
    }

    public <T> Object[][] tablify(String title, List<T> list) {
        int offset = title == null? 0 : 1;

        Object[][] table = new Object[list.size() + offset][1];
        if (title != null) {
            table[0][0] = title;
        }
        for (int i = 0; i < list.size(); i++) {
            table[i + offset][0] = list.get(i);
        }
        return table;
    }

    private static class ExternalDatabaseResolver {
        private ArrayList<ExternalDatabase> externalDatabases;
        private ArrayList<String> errorNames;

        public ExternalDatabaseResolver(ArrayList<ExternalDatabase> externalDatabases, ArrayList<String> errorNames) {
            this.externalDatabases = externalDatabases;
            this.errorNames = errorNames;
        }

        public ArrayList<ExternalDatabase> getExternalDatabases() {
            return externalDatabases;
        }

        public ArrayList<String> getErrorNames() {
            return errorNames;
        }
    }

    @ShellMethod("Create a user")
    public void createUser(@ShellOption(defaultValue = "NONE") String username) {
        if (username.equals("NONE")) {
            username = lineReader.readLine("Enter username (or q to exit): ");
            if (username.equals("q")) {
                return;
            }
        }
        boolean createdUser = userService.createUser(username);
        while (!createdUser) {
            terminal.writer().println("User '" + username + "' already exists or is an invalid name.");
            username = lineReader.readLine("Enter username (or q to exit): ");
            if (username.equals("q")) {
                return;
            }
            createdUser = userService.createUser(username);
        }
        terminal.writer().println("Created user '" + username + "'.");
        terminal.flush();
    }
}
