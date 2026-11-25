package com.spandan.llm_gateway_guard;

import java.util.Map;

public record ChatResponse(
        String reply, // CORE: generated LLM response
        String tenantId, // CORE: authenticated tenant
        Map<String, Integer> redactions // CORE: count of redactions per PII type
) {}
