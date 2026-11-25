# LLM Gateway Guard

A Spring Boot gateway that sits in front of LLM APIs to:
- redact sensitive data (PII) in prompts
- score prompt-injection risk
- track usage and cost per tenant
- stream events through Kafka into ClickHouse

**Status:** Work in progress, built in public. Daily progress is posted on LinkedIn.

## Stack
Java 21, Spring Boot 4, JWT, Kafka, ClickHouse, Docker Compose

## Roadmap
- [x] Week 1: Foundation (JWT Auth & Mock LLM Proxy)
- [ ] Week 2: Guardrails (PII Redaction & Injection Scoring)
- [ ] Week 3: Event pipeline
- [ ] Week 4: Dashboard and polish

## Guardrails: PII Redaction (Day 3)
- **API_KEY**: Redacts `sk-...` keys (at least 20 chars). Evaluated first to prevent numbers from being misidentified.
- **EMAIL**: Redacts email addresses matching standard pattern into `[EMAIL_REDACTED]`.
- **PHONE**: Redacts Indian mobile formats (+91 prefix, 10 digits starting with 6-9) and international formats into `[PHONE_REDACTED]`.

### Honest Limitations
Regex matching is linear-time and ReDoS-safe, but has trade-offs:
- **False Positives:** Non-phone 10-digit numbers starting with 6-9 (e.g. certain invoice or tracking numbers) may be masked as phone numbers.
- **False Negatives:** Obfuscated formats (e.g. `name at domain dot com`, or spelled-out numbers like `nine eight...`) bypass regex.
- **Scope:** Credit cards, Aadhaar, and PAN numbers are not yet supported.
