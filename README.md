# Car Rental (Java, JDBC, PostgreSQL)

A console application for managing customers, cars, and rentals with JDBC and a layered service/repository design.

## Requirements

- Java 17+
- Maven
- PostgreSQL

## Setup

1. Create the `car_rental_db` database.
2. Apply `resources/schema.sql`. The script is safe to re-run and does not drop tables.
3. Set the connection variables in your shell:

```sh
export DB_URL=jdbc:postgresql://localhost:5432/car_rental_db
export DB_USER=postgres
export DB_PASSWORD=your_local_password
```

4. Build and run:

```sh
mvn package
mvn exec:java -Dexec.mainClass=Main
```

The PostgreSQL JDBC driver is declared in Maven. Do not commit real database passwords or personal customer data.
