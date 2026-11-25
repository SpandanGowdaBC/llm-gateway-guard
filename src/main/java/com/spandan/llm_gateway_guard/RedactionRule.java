package com.spandan.llm_gateway_guard;

import java.util.regex.Pattern;

public record RedactionRule(
        String name, // CORE: identifier, e.g. "EMAIL"
        Pattern pattern, // CORE: pre-compiled regex pattern
        String placeholder // CORE: replacement token, e.g. "[EMAIL_REDACTED]"
) {}
