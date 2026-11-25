package com.spandan.llm_gateway_guard;

import org.springframework.stereotype.Service;

@Service // CORE: business logic layer
public class ChatService {

    private final LlmClient llmClient; // CORE: LLM provider
    private final PiiRedactor piiRedactor; // CORE: guardrail redactor

    public ChatService(LlmClient llmClient, PiiRedactor piiRedactor) {
        this.llmClient = llmClient;
        this.piiRedactor = piiRedactor;
    }

    public ChatResponse chat(String message) {
        // CORE: redact PII before passing text to the LLM
        RedactionResult redaction = piiRedactor.redact(message);
        // CORE: LLM only ever receives the safe, redacted prompt
        String reply = llmClient.generateReply(redaction.redactedText());
        String tenantId = TenantContext.getTenantId();
        return new ChatResponse(reply, tenantId, redaction.counts());
    }
}
