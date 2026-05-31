# Repository Guidelines

## Project Structure & Module Organization

This is a Java 17 Spring Boot MVC application for managing football tournaments. Source code lives under `src/main/java/it/uniroma3/siw/calcio`, organized by responsibility: `controller`, `model`, `repository`, `service`, `config`, `authentication`, and `exception`. Thymeleaf templates are in `src/main/resources/templates`, with admin views grouped under `templates/admin`. Static CSS, fonts, and images are in `src/main/resources/static`. Runtime local uploads go to `uploads/`; do not treat that directory as source assets. Tests live in `src/test/java`.

## Build, Test, and Development Commands

- `./mvnw spring-boot:run` or `.\mvnw.cmd spring-boot:run`: run the app locally on port `8080`.
- `./mvnw test`: run the JUnit/Spring Boot test suite.
- `./mvnw package`: build the application jar in `target/`.
- `docker compose up --build`: build and run the app with PostgreSQL using `docker-compose.yml`.

For local database configuration, copy `.env.example` to `.env` and set real passwords. The default non-Docker datasource points at `jdbc:postgresql://localhost:5432/calcio`.

## Coding Style & Naming Conventions

Use the existing Spring layering: controllers handle web requests, services contain business logic, repositories expose persistence, and entities stay in `model`. Follow Java conventions: 4-space indentation, `PascalCase` classes, `camelCase` fields and methods, and package names under `it.uniroma3.siw.calcio`. Keep Thymeleaf templates named by feature, such as `team/list.html` or `admin/player/edit-form.html`. Prefer constructor injection or established local patterns when adding dependencies.

## Testing Guidelines

Tests use JUnit 5 with Spring Boot test support. Name test classes after the unit or slice under test, ending in `Tests` or `Test`. Run `./mvnw test` before opening a pull request. Add focused tests for controller behavior, service rules, repository queries, and configuration changes that affect startup.

## Commit & Pull Request Guidelines

Recent commits use short imperative subjects, for example `Add image storage services` and `Fix local admin bootstrap`. Keep commits focused and describe the user-visible or technical change. Pull requests should include a brief summary, test results, linked issues when applicable, and screenshots for template or CSS changes.

## Security & Configuration Tips

Do not commit `.env`, secrets, database dumps, or generated files from `target/` or `uploads/`. Production image storage depends on `AZURE_STORAGE_CONNECTION_STRING` and `AZURE_STORAGE_CONTAINER_NAME`; document any new required environment variable in `.env.example` and relevant docs.
