package com.spandan.llm_gateway_guard;

import java.util.regex.Pattern;

public record InjectionRule(
        String name, // CORE: rule identifier, e.g. "IGNORE_INSTRUCTIONS"
        Pattern pattern, // CORE: pre-compiled regex pattern
        int weight // CORE: risk penalty weight (1 to 100)
) {}
