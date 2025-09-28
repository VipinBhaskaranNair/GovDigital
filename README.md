# RAG Chat Backend Microservice (Spring Boot)

## Overview

This microservice provides APIs to create and manage chat sessions and messages. Core features:

- Start and maintain chat sessions for users
- Save messages within a session (sender, content, optional context)
- Rename sessions
- Mark/unmark sessions as favorite
- Delete sessions and associated messages
- Retrieve message history (paged)

## Tech Highlights

- Spring Boot 3.2, Java 17
- PostgreSQL via Spring Data JPA
- API key authentication (header `X-API-KEY`)
- Rate limiting using Bucket4j
- Centralized logging (ELK)
- Dockerized for local development
- Swagger UI for API documentation
- Actuator endpoints for health checks and metrics
- Adminer DB tool available at http://localhost:8081 (containerized) to browse Postgres.
- Basic unit tests for service layer is included under `src/test/java` (use `./mvnw test`).
- CORS configured (currently allows all origins — can be restricted according to the requirement).

## Setup (local)

1. Copy `.env.example` to `.env.dev` and edit values:

2. Build locally (optional):
   ```bash
   mvn clean install
   ```
3. Start Postgres and the app using Docker Compose:
   ```bash
   ENVIRONMENT=dev docker-compose up
   Setting the environment variable ENVIRONMENT=dev will use the .env.dev file.
   This starts Postgres, ELK stack and builds the app container.

4. Use the API with the API key header:
   ```bash
   curl -H "X-API-KEY: your-secret" http://localhost:8080/api/v1/sessions
   ```

## API Summary

- `POST /api/v1/sessions` — create session `{ "title": "My conversation" }`
- `GET /api/v1/sessions` — list sessions
- `PATCH /api/v1/sessions/{id}/rename?title=New` — rename
- `PATCH /api/v1/sessions/{id}/favorite?favorite=true` — mark favorite
- `DELETE /api/v1/sessions/{id}` — delete session and messages
- `POST /api/v1/messages` — add message `{ sessionId, role, content, context? }`
- `GET /api/v1/messages/session/{sessionId}?page=0&size=50` — list messages

## Notes

- Rate limiting is per-API-key at 100 requests/min by default.

## Additional features added

- Health checks via Spring Boot Actuator (`/actuator/health`).
- Swagger/OpenAPI UI available at `/swagger-ui.html` (springdoc).
- Adminer DB tool available at http://localhost:8081 (containerized) to browse Postgres.
- Basic unit tests included under `src/test/java` (use `./mvnw test`).
- CORS configured (currently allows all origins — lock down in production).
- Pagination for messages supports `page` and `size` (max size capped at 200).
