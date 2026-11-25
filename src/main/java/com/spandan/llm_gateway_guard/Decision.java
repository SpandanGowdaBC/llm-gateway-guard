package com.spandan.llm_gateway_guard;

public enum Decision {
    ALLOW, // CORE: safe prompt, allow to proceed
    FLAG,  // CORE: mildly suspicious, allow but flag for monitoring
    BLOCK  // CORE: severe prompt injection threat, reject request
}
