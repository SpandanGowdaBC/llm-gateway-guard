# LLM Gateway Guard

A Spring Boot gateway that sits in front of LLM APIs to:
- redact sensitive data (PII) in prompts
- score prompt-injection risk
- track usage and cost per tenant
- stream events through Kafka into ClickHouse

**Status:** Work in progress, built in public. Daily progress is posted on LinkedIn.

## Stack
Java 21, Spring Boot 3, JWT, Kafka, ClickHouse, Docker Compose

## Roadmap
- [ ] Week 1: Foundation
- [ ] Week 2: Guardrails
- [ ] Week 3: Event pipeline
- [ ] Week 4: Dashboard and polish

## Note
Built with AI assistance as a learning project. Each part is reviewed and explained before moving on.