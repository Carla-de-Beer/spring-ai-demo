# Spring AI Demo

A small Spring Boot demo showcasing the Spring AI libraries (chat clients, chat memory, structured output, streaming and prompt templates).

This project provides several simple HTTP endpoints you can call locally to explore conversational AI features backed by the Spring AI starter libraries and an
OpenAI-compatible model.

The demo includes:

- A chat endpoint that uses prompt templates.
- A stateful chat endpoint that demonstrates chat memory stored in an H2 database.
- A streaming endpoint that returns streaming chat responses as Reactor Flux.
- A structured-output example that requests JSON data (fruit tree information) from the model and maps it to Java records.
- Prompt template examples (in `src/main/resources/prompttemplates`).

## Architecture Overview

```
User → Controller (REST) → ChatClient (Spring AI) → Model/Tool (OpenAI/Claude/Mistral) → Response
```

Endpoints use Spring AI's ChatClient, optionally with tools, memory, and prompt templates. 
Tool endpoints extract structured data from user input and create records in the database.

## What this project demonstrates

- spring-ai `ChatClient` configuration and custom advisors (e.g. token usage logging).
- Chat memory using JDBC repository with H2 (persisting conversation history between requests).
- Prompt templates (system and user templates) and parameter substitution.
- Streaming responses using Reactor Flux.
- Structured output mapping to Java records via ChatClient entity binding.

## Prerequisites

- Java 21
- Spring Boot 3.4.x
- Maven (the project includes the Maven wrapper `mvnw` so you don't need a global Maven install)
- An OpenAI-compatible API endpoint or local mock that implements the same API surface.
- By default the app's `application.yml` points to a Docker model with `http://localhost:12434/engines` — adjust this to your provider or point your local
  mock/proxy there.
- A model-compatible API key if your provider requires it.

### Environment Variables

| Variable                        | Default/Example                | Description                        |
|----------------------------------|-------------------------------|------------------------------------|
| SPRING_AI_OPENAI_API_KEY        | `test-key`                    | API key for OpenAI-compatible API  |
| SPRING_AI_OPENAI_BASE_URL       | `http://localhost:12434/engines` | Base URL for model API         |


## Configuration

Important configuration lives in `src/main/resources/application.yml`.

Key properties:

- `spring.ai.openai.api-key` — the API key to use (the YAML shows `OPEN_AI_API_KEY` placeholder).
- `spring.ai.openai.base-url` — base URL for your OpenAI-compatible provider (default in this project: `http://localhost:12434/engines`).
- `spring.ai.chat.options.model` — the default model name used (config uses `ai/mistral`); change it to a model your provider exposes.
 - To use Claude Sonnet 3.5, set `spring.ai.chat.options.model=claude-sonnet-3.5` in your `application.yml` or as an environment variable.
- H2 datasource properties for chat memory are configured in the YAML and persist to `~/chatmemory` by default.

You can override properties with environment variables. Example (macOS / zsh):

```bash
export SPRING_AI_OPENAI_API_KEY="your_api_key_here"
export SPRING_AI_OPENAI_BASE_URL="https://api.openai.com/v1"

./mvnw spring-boot:run
```

## Run locally

Using the included Maven wrapper:

```bash
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

The application starts on port 8080 by default.

## Available endpoints

All endpoints are under `/api`.

- GET /api/chat?message=...  
  Simple chat using a system template for an estate agent search. Returns the raw content string.

- GET /api/chat-memory (headers: `username`, query: `message`)  
  Stateful chat that stores and retrieves conversation history for the supplied `username` using the JDBC chat memory repository backed by H2. Example:

  curl -H "username: alice" "http://localhost:8080/api/chat-memory?message=Hello"

- GET /api/stream?message=...  
  Streams a chat response as a Reactor Flux.

- GET /api/structured-output?location=...  
  Asks the model for structured JSON describing fruit trees in a given location and attempts to bind the JSON to `FruitTreeInfo` records. Returns JSON.

- GET /api/service-request (headers: `username`, param: `message`)
  - Uses a tool-enabled ChatClient to extract a structured service request from user input and create a record in the database.
  - Example:
    ```bash
    curl -X GET -H "username: alice" "http://localhost:8080/api/service-request?message=My laptop is broken and I need urgent help"
    ```

Example curl call (chat):

```bash
curl "http://localhost:8080/api/chat?message=Find+3+homes+under+250k+in+Marseilles"
```

Matching Postman-based queries can be found here: ./postman-requests/spring-ai.postman_collection.json.

## Files of interest

- `src/main/java/dev/cadebe/springaidemo/config/springai/ChatClientConfig.java` — builds a default `ChatClient` with advisors and default system/user content.
- `src/main/java/dev/cadebe/springaidemo/config/springai/ChatMemoryChatClientConfig.java` — demonstrates a stateful chat client that uses
  `JdbcChatMemoryRepository` and `MessageWindowChatMemory`.
- `src/main/java/dev/cadebe/springaidemo/controller/*` — controllers exposing the demo endpoints.
- `src/main/resources/prompttemplates/` — prompt templates used by the demo (system and user templates). See `fruit-tree-info-template.st` for the structured
  output prompt.
- `src/main/resources/schema/h2-db.sql` — schema initialization used by the JDBC chat memory repository. By default H2 is configured to initialize this schema
  on startup.

## Postman & Automated Testing

- Import the Postman collection from `./postman-requests/spring-ai.postman_collection.json`.
- Each request includes tests for HTTP 200 and non-empty response.
- To run from CLI:
  ```bash
  npm install -g newman
  newman run postman-requests/spring-ai.postman_collection.json
  ```

## Running Tests

Run all tests:
```bash
./mvnw test
```
Unit and integration tests cover controller, tool, and service logic.

## Troubleshooting

- If you see connection errors, check your model base URL and API key.
- If H2 fails to start, ensure no other process is locking the DB file.
- If the model returns invalid JSON for tool endpoints, review the system prompt template for strictness.

## Troubleshooting & notes

- If you see connection errors to the AI provider, verify `spring.ai.openai.base-url` and the API key.
- Model name compatibility: the code sets `ai/mistral` as the model in `application.yml`. Replace this with a model name your provider exposes.
- H2 persistence: the database file defaults to `~/chatmemory` (see `application.yml`). If you prefer in-memory only for testing, change the JDBC URL to
  `jdbc:h2:mem:testdb`.
