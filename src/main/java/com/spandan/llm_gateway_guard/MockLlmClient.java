package com.spandan.llm_gateway_guard;

import org.springframework.stereotype.Component;

@Component // CORE
public class MockLlmClient implements LlmClient {

    @Override
    public String generateReply(String prompt) {
        return "Mock reply to: " + prompt; // CORE
    }
}
