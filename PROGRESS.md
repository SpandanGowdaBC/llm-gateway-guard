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
- Day 0: Understood project structure (pom.xml, application.properties, Application class). Added `/ping` health endpoint in PingController. Added automated web-layer slice test in PingControllerTest with MockMvc.
- Day 1: Built JWT Tenant Resolution layer without Spring Security using JJWT 0.12.6. Implemented `TenantContext` (ThreadLocal), `JwtAuthFilter` (`OncePerRequestFilter` protecting `/api/**`), and `WhoAmIController` (`GET /api/whoami`). Created automated test suite with 7 test cases in `WhoAmIControllerTest`.
- Day 2: Built Mock LLM Client behind the Strategy Pattern (`LlmClient` interface and `MockLlmClient`), `ChatService` with constructor injection, immutable Java 21 DTO records (`ChatRequest`, `ChatResponse`), `ChatController` (`POST /api/chat`) returning reply + `tenantId`, and `GlobalExceptionHandler` (`@RestControllerAdvice`) returning standardized 400 Bad Request JSON errors. Added 6 new automated tests (`ChatControllerTest`, `MockLlmClientTest`), bringing the total test suite to 15 passing tests.

## Concepts I understand (in my own words)
- Maven builds the project and downloads libraries; Spring Boot sets up the web server.
- `@GetMapping` routes an incoming HTTP GET request to a specific method.
- An assertion compares what the server returned vs what was expected; if they differ, the test will "fail".
- If we forget to clear the `ThreadLocal`, "the data of the previous tenant will be leaked" across reused threads in the pool.
- Public endpoints work because `shouldNotFilter` skips routes that don't start with `/api/` ("ping can be accessed by anyone even without a jwt token").
- The `finally` block is an ironclad contract: "yes it will execute bcoz finally block will execute even if the upper statement fails".
- Separation of concerns: the controller doesn't parse headers "because the verification is already done by authfilter it just need to check for badge and leave it".
- Interface contract: if a class implements an interface without the method, "it will stop completely and throw compilation error".
- Strategy Pattern & Dependency Injection: ChatService depends on the interface "because we are following design strategy where we can change the mockclient but the interface will be same like socket".
- DTO records: "it replaces traditional java boilerplate with clean immutable record".
- Request lifecycle: request flows through "filter controller service client".
- Centralized exception handling: without `@RestControllerAdvice`, "it wont show us the specific error or which controller not working".

## Concepts I'm still shaky on
- Distinguishing boundary validation annotations (`@NotBlank` for empty/whitespace floor vs `@Size(max)` for ceiling).
- Standalone pure Java unit tests vs Spring-boot managed integration tests.

## Next up
- Week 2: Guardrails layer (PII redaction, prompt-injection risk scoring, allow/block rules, and guardrail test suites).
