package com.spandan.llm_gateway_guard;

public record ChatResponse(
        String reply, // CORE
        String tenantId // CORE
) {}
