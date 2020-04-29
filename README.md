# The Big Movie Backend

## Command Line Interface (CLI)

### Commands

Strings with spaces in them must be surrounded with quotes or they will be interpreted as separate parameters.

- create-user: Create a user
    - params: [username]
    - if username is excluded or "NONE", user is prompted for username. "q" is also not a valid username.

- delete-list: Delete list
    - params: [listname]
    - if listname is excluded or "NONE", defaults to most recent list.

- echo: Echo locally stored values
    - params [value]
    - value accepts the following values
        - user - returns the current user
        - search - returns the list of the most recent search results
        - list - returns the most recent list
        - \<int> - returns the value index from the most recent search results. 

- list-all: List all lists
    - no params

- list-dbs: List all databases
    - no params

- list-list: Display a list
    - params: [name] [username]
    - name refers to the name of the list
    - if name is excluded or "NONE", defaults to the most recent list
    - if username is excluded or "NONE", defaults to the logged in user

- list-lists: List all lists for a user
    - params: [username]
    - if username is excluded or "NONE", then it returns all lists for currently logged in user.

- list-movies: List all movies in internal database
    - no params

- list-users: Get all users
    - no params

- login: Sets the current user to a user with the specified username
    - params: username

- logout: Log out
    - no params

- make-list: Make list
    - params: name
    - must be logged in to call this

- remove: Remove item from list
    - params: index [listname]
    - index refers to the index of the movie you want to remove from the list. Index is 1-based.
    - listname is the name of the list to remove from. If excluded or "NONE", defaults to most recent list

- save: Save an item from recent results
    - params: index [listname]
    - index refers to the index of the movie you want to save to the list from the most recent search results. Index is 1-based.
    - listname is the name of the list to save the movie to. If excluded or "NONE", defaults to most recent list

- save-raw: Explicitly save a movie to the local database (not recommended; for debug purposes)
    - params: movieName
    - saves movie with only a title

- search: Search for a movie, television show, or actor.
    - params: query [-db database]
    - if db is 'all' or excluded, then defaults to searching all databases. For multiple dbs, separate by comma

- search-users: Search for a user
    - params: query

## REST API

Available at: https://cs371-backend.herokuapp.com/

### GET requests

`/user` Get all users

`/user?username=[someusername]` Search users by username

`/user/id/` Get a specific user

`/user/id/list` Get all lists from a specific user

`/user/id/list?name=[somelistname]` Get a specific list by a user by name

`/list` Get all lists

`/list/id/` Get a specific list

`/movie` Get all movies in the internal db

`/movie?title=[sometitle]` Search all dbs for a movie

`/movie?title=[sometitle]&dbs=[somedbs]` Search specific dbs for movie

`/movie/id/` Get a specific movie from the internal db (used for reference with lists)

`/comment?movieid=[somemovieid]` Get all comments for a movie

`/comment?userid=[someuserid]` Get all comments for a user

### POST requests

`/user` Create user

`/list` Create list

`/comment` Create comment

### DELETE requests

`/list?userid=[useridforlist]` Delete list. User must be the user for the corresponding list.
