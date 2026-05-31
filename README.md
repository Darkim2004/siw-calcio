# SIW Calcio

SIW Calcio is a web application for managing and browsing a football portal. The project lets users view teams, players, tournaments, and matches, while administrators can create and update the main domain data.

The application is built with Java and Spring Boot, uses PostgreSQL as its relational database, Thymeleaf for server-side views, and Docker for containerized execution. In production, it is designed to run on Azure App Service with Azure Database for PostgreSQL and Azure Blob Storage for uploaded images.

## Main Features

- Home page with highlighted matches and portal content.
- Team list and team detail pages, including squad and logo.
- Tournament list and tournament detail pages, including participating teams and points.
- Match list and match detail pages, including status, score, teams, tournament, venue, and referee.
- User registration and login.
- Authenticated user comments on matches.
- Admin area for creating, editing, and deleting teams, players, tournaments, matches, and referees.
- Image uploads for team logos and player photos.

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 4 |
| Web MVC | Spring Web MVC |
| Template engine | Thymeleaf |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL |
| Security | Spring Security, BCrypt |
| Validation | Spring Validation |
| Image storage | Local file system in development, Azure Blob Storage in production |
| Containers | Docker, Docker Compose |
| Cloud | Azure App Service, Azure Database for PostgreSQL, Azure Blob Storage |
| Build | Maven Wrapper |

## Project Structure

The project follows a typical Spring MVC structure:

```text
src/main/java/it/uniroma3/siw/calcio
|-- authentication   # Spring Security configuration
|-- config           # App, local storage, Azure Blob, and demo data configuration
|-- controller       # MVC controllers and HTTP routing
|-- exception        # Application error handling
|-- model            # JPA domain entities
|-- repository       # Spring Data JPA repositories
`-- service          # Application business logic
```

HTML pages are stored in `src/main/resources/templates`, while CSS, fonts, and static images are stored in `src/main/resources/static`.

The main domain model includes:

- `Team`: football team, city, foundation year, logo, and squad.
- `Player`: player profile, role, squad number, height, birth date, photo, and team.
- `Tournament`: tournament with year, description, and team participations.
- `Partecipation`: association between a team and a tournament, including points.
- `Match`: match between two teams, with tournament, referee, score, state, and venue.
- `Referee`: match referee.
- `Comment`: user comment on a match.
- `User`: application user with either `ROLE_USER` or `ROLE_ADMIN`.

## Profiles and Image Storage

The application uses two image storage strategies:

- Non-`prod` profile: `LocalImageStorageService` stores files under the local `uploads/` directory and exposes them through `/uploads/**`.
- `prod` profile: `AzureBlobImageStorageService` uploads images to Azure Blob Storage and stores the public blob URL in the database.

Accepted image formats are `.jpg`, `.jpeg`, `.png`, and `.webp`, with a configured upload limit of `5MB`.

In production, the configured Azure Blob container is created automatically if it does not already exist. The public access level must still be configured in Azure. With the current application design, the container must allow public read access to individual blobs because Thymeleaf templates render image URLs directly inside `<img>` tags.

## Prerequisites

- Java 17
- Docker and Docker Compose
- PostgreSQL, if running without Docker
- An Azure account, for cloud deployment

## Environment Configuration

For Docker Compose, start from the example environment file:

```bash
cp .env.example .env
```

Then configure at least:

```text
POSTGRES_DB=calcio
POSTGRES_USER=calcio
POSTGRES_PASSWORD=<local-db-password>
APP_ADMIN_USERNAME=admin
APP_ADMIN_PASSWORD=<admin-password>
AZURE_STORAGE_CONNECTION_STRING=<azure-storage-connection-string>
AZURE_STORAGE_CONTAINER_NAME=images
SPRING_PROFILES_ACTIVE=prod
```

The `.env` file contains local secrets and must not be committed.

## Running With Docker Compose

The `docker-compose.yml` file starts:

- a PostgreSQL 16 container;
- the Spring Boot application container;
- a persistent volume for PostgreSQL data.

Start the stack with:

```bash
docker compose up --build
```

The application will be available at:

```text
http://localhost:8080
```

PostgreSQL is exposed locally on port `5433`.

Note: the Compose setup uses `SPRING_PROFILES_ACTIVE=prod`, so it requires a valid Azure Blob Storage connection string. If you want to develop without Azure Blob Storage, run the application with the default profile and use local storage under `uploads/`.

## Running Locally Without Docker

Make sure PostgreSQL is running and reachable. The default configuration uses:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/calcio
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

You can override these variables from your terminal or IDE.

Start the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

With the default profile, uploaded images are saved in the local `uploads/` directory.

## Demo Data and Admin User

The application can create or update an admin user on startup when `APP_ADMIN_PASSWORD` is set.

To seed demo data:

```text
APP_SEED_DEMO_DATA=true
APP_ADMIN_USERNAME=admin
APP_ADMIN_PASSWORD=<admin-password>
```

Demo seeding runs only when the main domain tables are empty.

During development with `ddl-auto=create`, Hibernate can also use `src/main/resources/import.sql` to load initial data.

## Tests and Build

Run tests:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

Build the jar:

```bash
./mvnw clean package
```

The Dockerfile uses a multi-stage build:

1. `eclipse-temurin:17-jdk` compiles the project;
2. `eclipse-temurin:17-jre` runs the final jar.

## Azure Deployment

Production configuration lives in `src/main/resources/application-prod.properties`.

The intended Azure deployment uses:

- Azure App Service to run the Java/Spring Boot app or Docker container.
- Azure Database for PostgreSQL as the application database.
- Azure Blob Storage for team logos and player photos.

Set the following environment variables in the Azure Web App:

```text
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=<azure-postgres-jdbc-url>
SPRING_DATASOURCE_USERNAME=<postgres-user>
SPRING_DATASOURCE_PASSWORD=<postgres-password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
AZURE_STORAGE_CONNECTION_STRING=<storage-account-connection-string>
AZURE_STORAGE_CONTAINER_NAME=images
APP_ADMIN_USERNAME=admin
APP_ADMIN_PASSWORD=<admin-password>
APP_SEED_DEMO_DATA=false
```

Azure App Service exposes the port through `WEBSITES_PORT`; the application also supports `PORT` as a fallback.

## Azure Blob Storage

For image storage, create a Storage Account and a container, for example `images`.

The application reads the storage connection string from `AZURE_STORAGE_CONNECTION_STRING`. Never store this value in the repository.

More details are available in [docs/azure-blob-storage.md](docs/azure-blob-storage.md).

## Security

Spring Security manages authentication and authorization:

- public pages: home, login, registration, teams, tournaments, matches, and static assets;
- comments: available to authenticated users;
- `/admin/**`: restricted to users with `ROLE_ADMIN`;
- passwords are stored with BCrypt.

## Operational Notes

- Local uploads are ignored by Git through `uploads/`.
- Secrets must be stored in a local `.env` file or in Azure environment variables.
- Avoid `ddl-auto=create` in production. Use `update` only for simple deployments, or introduce a dedicated migration strategy for more structured environments.
