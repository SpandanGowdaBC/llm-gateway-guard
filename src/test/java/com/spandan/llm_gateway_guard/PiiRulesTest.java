package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PiiRulesTest {

    private final RedactionRule emailRule = new PiiRules().emailRule();

    private String redact(String input) {
        // CORE: replace all regex matches with the placeholder
        return emailRule.pattern().matcher(input).replaceAll(emailRule.placeholder());
    }

    @Test
    void redactsSingleEmail() {
        String input = "Contact me at john@example.com for help";
        assertEquals("Contact me at [EMAIL_REDACTED] for help", redact(input));
    }

    @Test
    void redactsMultipleEmails() {
        String input = "Send files to alice@acme.com and bob@work.org";
        assertEquals("Send files to [EMAIL_REDACTED] and [EMAIL_REDACTED]", redact(input));
    }

    @Test
    void leavesPlainTextWithoutEmailUnchanged() {
        String input = "Hello this is a plain message with no secrets";
        assertEquals(input, redact(input));
    }
}
