package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PhoneRuleTest {

    private final RedactionRule phoneRule = new PiiRules().phoneRule();

    private String redact(String input) {
        // CORE: replace matches with phone placeholder
        return phoneRule.pattern().matcher(input).replaceAll(phoneRule.placeholder());
    }

    @Test
    void redactsIndianPhoneWithCountryCode() {
        assertEquals("Call me at [PHONE_REDACTED] tomorrow",
                redact("Call me at +91 98765 43210 tomorrow"));
    }

    @Test
    void redactsIndianMobileWithoutCountryCode() {
        assertEquals("Call me at [PHONE_REDACTED] please",
                redact("Call me at 9876543210 please"));
    }

    @Test
    void redactsInternationalFormat() {
        assertEquals("US office is at [PHONE_REDACTED]",
                redact("US office is at +1-555-123-4567"));
    }

    @Test
    void leavesHarmlessOrderNumberUnchanged() {
        assertEquals("Your order number is 1002345678",
                redact("Your order number is 1002345678"));
    }

    @Test
    void leavesPlainStringUnchanged() {
        assertEquals("Let's meet at the park tomorrow afternoon",
                redact("Let's meet at the park tomorrow afternoon"));
    }
}
