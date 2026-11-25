package com.spandan.llm_gateway_guard;

import org.springframework.stereotype.Service;

@Service // CORE: business logic layer
public class ChatService {

    private final LlmClient llmClient;
    private final PiiRedactor piiRedactor;
    private final InjectionScorer injectionScorer;

    public ChatService(LlmClient llmClient, PiiRedactor piiRedactor, InjectionScorer injectionScorer) {
        this.llmClient = llmClient;
        this.piiRedactor = piiRedactor;
        this.injectionScorer = injectionScorer;
    }

    public ChatResponse chat(String message) {
        // CORE 1: scan for injection before touching or redacting the text
        InjectionResult injectionResult = injectionScorer.score(message);
        if (injectionResult.decision() == Decision.BLOCK) {
            throw new PromptInjectionException("Prompt injection detected: request blocked");
        }

        // CORE 2: sanitize sensitive PII only on allowed or flagged prompts
        RedactionResult redaction = piiRedactor.redact(message);

        // CORE 3: LLM client only ever receives safe, sanitized text
        String reply = llmClient.generateReply(redaction.redactedText());
        String tenantId = TenantContext.getTenantId();

        return new ChatResponse(
                reply,
                tenantId,
                redaction.counts(),
                injectionResult.score(),
                injectionResult.decision(),
                injectionResult.matchedRules()
        );
    }
}
