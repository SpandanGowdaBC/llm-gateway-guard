# PROGRESS

Project: LLM Gateway Guard
Stack: Java 21, Spring Boot 4, JWT, Kafka, ClickHouse, Docker Compose

## Roadmap
- Week 1: Foundation (Spring Boot app, JWT login, tenant resolution, /chat endpoint to a mock LLM)
- Week 2: Guardrails (PII redaction, prompt-injection scoring, allow/block rules, tests)
- Week 3: Pipeline (events to Kafka, batch consume, store in ClickHouse)
- Week 4: Visibility and polish (usage and cost per tenant, dashboard, Docker Compose, README, diagram)

## Done so far
- Project skeleton generated and running (Spring Boot 4.2.0, Java 21)
- Day 0 completed: Understood project structure (pom.xml, application.properties, Application class). Added `/ping` health endpoint in PingController returning "gateway is alive". Added automated web-layer slice test in PingControllerTest using MockMvc.

## Concepts I understand (in my own words)
- Maven builds the project and downloads libraries.
- Spring Boot sets up the web server for us.
- `@GetMapping` routes an incoming HTTP GET request to a specific method.
- An assertion compares what the server returned vs what was expected; if they differ, the test will "fail".

## Concepts I'm still shaky on
- MockMvc chaining syntax (`.andExpect(content().string(...))`) and writing assertions without hints.
- Port conflicts and identifying background processes hogging port 8080.
- Spring Boot 4 modular test starters vs Spring Boot 3 monolithic starters.

## Next up
- Day 1: Laying the foundation for request handling (mock LLM client or `/chat` endpoint skeleton with tenant ID resolution).
