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
- Day 3: Built linear-time PII Redaction Guardrail layer before external LLM invocation. Created `RedactionRule` and `RedactionResult` records. Implemented `PiiRules` with `@Bean` configuration for `API_KEY` (`sk-...`), `EMAIL`, and `PHONE` (Indian mobile and international formats) with `@Order` annotations. Built `PiiRedactor` pipeline applying rules sequentially and recording counts. Wired redactor into `ChatService` and `ChatResponse` (`redactions` map), ensuring the LLM client only ever receives sanitized text. Added honest limitations section to `README.md`. Added 15 new tests (`PiiRulesTest`, `PhoneRuleTest`, `ApiKeyRuleTest`, `PiiRedactorTest`, `ChatControllerTest`), bringing total suite from 15 to 30 passing tests.

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
- Pre-compiling regex into records: "by storing inside redactionrule we dont need keep passing evrytime a new request comes by using this java compiles once when application reboot".
- Replacing all matches: "it replaces all the email present in message as it scans the complete message and replaces all with our pattern".
- Digit constraints: "6 to 9 means the starting numbers should be from 6 to 9 for india numbers and it ensures 6 to 9 numbers".
- Minimum quantifiers: `{20,}` ignores short abbreviations like `sk-test`, but catches full secret keys and replaces them.
- Rule ordering: `API_KEY` must run before `PHONE` because the "number part" inside an API key could be mistaken for a phone number if phone ran first.
- Pre-call redaction: "th secret would be passed to llmclient otherwise to prevent dataleakge we need make redaction before llmclient.generatereply".
- Breaking tests intentionally: running the LLM before redactor causes `chatEndpoint_redactsEmailAndDoesNotExposeOriginalInReply` to fail because raw secrets leak into the LLM output.
- False negative concept: a detector staying silent and missing real sensitive data like `alice [at] company [dot] com` is a "false negative".

## Concepts I'm still shaky on
- Regex quantifier syntax: `{n,}` with a trailing comma means "at least n" (not "only n").
- Remembering false positives (false alarm on harmless text) vs false negatives (missed secret that leaks through).

## Next up
- Week 2 Guardrails continuation: Prompt-injection risk scoring (scoring prompt risk before sending to LLM) and allow/block rules.
