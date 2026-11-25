package com.spandan.llm_gateway_guard;

import java.util.List;

public record InjectionResult(
        int score, // CORE: total risk score capped at 100
        List<String> matchedRules, // CORE: names of triggered rules
        Decision decision // CORE: final verdict (ALLOW, FLAG, BLOCK)
) {}
