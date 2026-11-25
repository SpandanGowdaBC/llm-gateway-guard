package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiKeyRuleTest {

    private final RedactionRule apiKeyRule = new PiiRules().apiKeyRule();

    private String redact(String input) {
        // CORE: replace matches with API key placeholder
        return apiKeyRule.pattern().matcher(input).replaceAll(apiKeyRule.placeholder());
    }

    @Test
    void redactsValidApiKey() {
        assertEquals("Here is my key [API_KEY_REDACTED] please use it",
                redact("Here is my key sk-abcdef1234567890123456 please use it"));
    }

    @Test
    void leavesShortPrefixUnchanged() {
        assertEquals("Our project code is sk-test",
                redact("Our project code is sk-test"));
    }

    @Test
    void leavesPlainStringUnchanged() {
        assertEquals("Tell me a joke about programming",
                redact("Tell me a joke about programming"));
    }
}
