package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PiiRedactorTest {

    private final PiiRules piiRules = new PiiRules();
    // CORE: pass rules in strict priority order (API_KEY first)
    private final PiiRedactor redactor = new PiiRedactor(List.of(
            piiRules.apiKeyRule(),
            piiRules.emailRule(),
            piiRules.phoneRule()
    ));

    @Test
    void redactsMixedMessageAndTracksCounts() {
        String input = "Key sk-12345678901234567890, email john@work.com, phone 9876543210";
        RedactionResult result = redactor.redact(input);

        String expectedText = "Key [API_KEY_REDACTED], email [EMAIL_REDACTED], phone [PHONE_REDACTED]";
        assertEquals(expectedText, result.redactedText());
        assertEquals(Map.of("API_KEY", 1, "EMAIL", 1, "PHONE", 1), result.counts());
    }

    @Test
    void appliesApiKeyBeforePhoneSoKeyDigitsAreNotTreatedAsPhone() {
        String input = "Key sk-abcdef9876543210ghijkl";
        RedactionResult result = redactor.redact(input);

        assertEquals("Key [API_KEY_REDACTED]", result.redactedText());
        assertEquals(Map.of("API_KEY", 1), result.counts());
    }
}
