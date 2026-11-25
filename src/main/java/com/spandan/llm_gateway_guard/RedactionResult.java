package com.spandan.llm_gateway_guard;

import java.util.Map;

public record RedactionResult(
        String redactedText, // CORE: safe sanitized text
        Map<String, Integer> counts // CORE: rule name -> number of redactions
) {}
