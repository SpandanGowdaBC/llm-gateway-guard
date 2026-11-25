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
- [x] Week 2: Guardrails (PII Redaction & Injection Scoring)
- [ ] Week 3: Event pipeline
- [ ] Week 4: Dashboard and polish

## Guardrails: PII Redaction (Day 3)
- **API_KEY**: Redacts `sk-...` keys (at least 20 chars). Evaluated first to prevent numbers from being misidentified.
- **EMAIL**: Redacts email addresses matching standard pattern into `[EMAIL_REDACTED]`.
- **PHONE**: Redacts Indian mobile formats (+91 prefix, 10 digits starting with 6-9) and international formats into `[PHONE_REDACTED]`.

### Honest Limitations: PII Redaction
Regex matching is linear-time and ReDoS-safe, but has trade-offs:
- **False Positives:** Non-phone 10-digit numbers starting with 6-9 (e.g. certain invoice or tracking numbers) may be masked as phone numbers.
- **False Negatives:** Obfuscated formats (e.g. `name at domain dot com`, or spelled-out numbers like `nine eight...`) bypass regex.
- **Scope:** Credit cards, Aadhaar, and PAN numbers are not yet supported.

## Guardrails: Prompt-Injection Risk Scoring (Day 4)
- **Normalizer**: Collapses whitespace, lowercases text, and strips invisible zero-width characters (`\u200B`–`\u200D`, `\uFEFF`) and control bytes to defeat evasion.
- **Rules & Weights**:
  - `IGNORE_INSTRUCTIONS` (weight 50): Commands attempting to erase system prompts (`ignore/disregard/forget` + `all/previous/prior` + `instructions/rules/prompts`).
  - `REVEAL_SYSTEM_PROMPT` (weight 50): Extraction attempts targeting system prompts (`show/reveal/repeat` + `system/initial/hidden prompt`).
  - `ROLE_OVERRIDE` (weight 40): Jailbreak persona switches (`act as/you are now` + `developer mode/dan/unrestricted/jailbreak`).
  - `DELIMITER_ABUSE` (weight 45): Forged tokenizer or markdown headers (`<|im_start|>`, `<|endoftext|>`, `### system`).
- **Dynamic Thresholds**: Configurable via `application.properties` (default: `FLAG` >= 30, `BLOCK` >= 70). Scores capped at 100.
- **Early Exit**: Blocked requests throw `PromptInjectionException` (HTTP 403 Forbidden) and bypass both PII redaction and `LlmClient`.

### Honest Limitations: Injection Scoring
- **False Negatives (Missed Attacks)**:
  - **Foreign Languages**: Prompts translated into French, Spanish, or Hindi bypass English-only regex.
  - **Semantic Paraphrasing**: Phrasings that avoid exact keywords (e.g. *"Start over with a blank slate and discard your upbringing"*) convey attack intent to the LLM but bypass regex.
  - **Encoded Payloads**: Base64 or rot13 encoded prompts (`SWdub3Jl...`) are not decoded prior to scanning.
- **False Positives (False Alarms)**:
  - AI security research prompts discussing jailbreaks like "DAN" or quoting `### system` headers may be flagged or blocked.
