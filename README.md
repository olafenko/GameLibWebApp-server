# Game Library Web Application - Server

**Game Library Web Application - Server** is a web application that allows users to browse and manage a library of games. It offers browsing features for guests and extended game management options for logged-in users, such as adding, editing, deleting, and rating games. Each new or updated game requires approval from an administrator.

## Features

- **Public game browsing** - available without login
- **User registration and login**
- **Adding, deleting, and updating games** - available only to logged-in users
- **Rating games** - available to logged-in users
- **Admin approval** - new and updated games require administrator approval

## Technologies

The application is built using the following technologies:

- **Spring Boot** - main application framework
- **MySQL** - relational database
- **Hibernate** - ORM (object-relational mapping)
- **Flyway** - database migrations and initialization, including the creation of an admin account
- **Spring Security** and **JWT** - for user authentication and authorization

## Testing Technologies

The application uses the following technologies for testing:

- **JUnit** and **AssertJ** - for assertions and unit tests
- **Mockito** - for mocking dependencies in unit tests
- **MockMvc** - for integration testing of controllers
- **Testcontainers** - enables testing with a real MySQL database in a container (instead of H2, due to enum conversion issues)

## API Endpoints

All API endpoints start with the prefix `/api/`.

### Authentication

- `POST /api/auth/register` - Registers a new user
- `POST /api/auth/login` - Logs in a user, generates JWT

### Games (`/api/games`)

- `POST /api/games/add` - Adds a new game (for logged-in users; game requires admin approval)
- `GET /api/games/all` - Displays all approved games
- `GET /api/games/top-three` - Displays the top three highest-rated games
- `PUT /api/games/update/{id}` - Updates game details (for logged-in users; requires admin approval)
- `DELETE /api/admin/reject/{id}` - Rejects and deletes the selected game

### Ratings (`/api/ratings`)

- `POST /api/ratings/rate` - Adds or updates a game rating (for logged-in users)

## Testing

To run tests locally, you need Docker installed and running on your machine, as **Testcontainers** relies on Docker to manage services (such as MySQL) used during tests.

Testcontainers will automatically download the necessary Docker images (e.g., MySQL) and start containers for testing. Once the tests are completed, the containers will be stopped and removed.

If Docker is not running, tests that require Testcontainers will fail.


