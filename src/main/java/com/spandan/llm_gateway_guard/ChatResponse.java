package com.spandan.llm_gateway_guard;

import java.util.List;
import java.util.Map;

public record ChatResponse(
        String reply, // CORE: generated LLM response
        String tenantId, // CORE: authenticated tenant
        Map<String, Integer> redactions, // CORE: count of redactions per PII type
        int riskScore, // CORE: prompt injection risk score (0-100)
        Decision decision, // CORE: ALLOW or FLAG (BLOCK is rejected)
        List<String> matchedRules // CORE: names of matched injection rules
) {}
