# To-Do Web App with Spring Boot

A small to-do list web app I built while learning Java and Spring Boot. You can list tasks, search them by user, due date or status, add new ones, see their details and delete them.

The repo has three folders, one for each step of the exercise. The pages are the same in all of them; what changes is where the tasks are stored:

1. **`01-version-memoria`** – tasks kept in memory (they're lost when the app restarts).
2. **`02-version-mariadb`** – tasks stored in a MariaDB database using plain SQL.
3. **`03-version-persistencia`** – tasks stored in a local file with the Perst object database.

**Just to be clear:** this isn't a real product and it's not meant to be used for anything serious. It's a practice project I made to learn the language and the framework. Some names in the code (and the sample categories) are still in Spanish/Valencian because the templates and the database depend on them.

## Running it

Each version is a separate Maven project. From inside one of the version folders:

```bash
./mvnw spring-boot:run
```

Then open http://localhost:8080. The MariaDB version needs its own database set up first.
