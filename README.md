# Car Parking

A Java client-server parking ticket application. The server stores parking
tickets in Apache Derby, while the Swing client adds and retrieves tickets
over an object-stream connection.

## Requirements

- Java 17
- Maven
- Apache Derby Network Server running on `localhost:1527`
- A Derby database named `ProjectDB`

The server creates the `PARKING_TICKET` table automatically when it starts.

## Database configuration

The server reads the database credentials from environment variables:

```powershell
$env:PARKING_DB_USERNAME = "your-username"
$env:PARKING_DB_PASSWORD = "your-password"
```

Do not commit database credentials to the repository.

## Running the application

Start the server first:

```powershell
cd StarterServerProject
mvn compile exec:java
```

Then start the client in a second terminal:

```powershell
cd StarterClientProject
mvn compile exec:java
```

The server listens on port `5050`. The client provides controls for adding
tickets and retrieving all stored tickets.

## Project structure

```text
StarterServerProject/   Derby database access and socket server
StarterClientProject/   Swing GUI and socket client
```
