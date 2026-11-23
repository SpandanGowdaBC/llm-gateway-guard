package com.spandan.llm_gateway_guard;

import org.springframework.stereotype.Service;

@Service // CORE: business logic layer
public class ChatService {

    private final LlmClient llmClient; // CORE: depends on the interface

    public ChatService(LlmClient llmClient) { // CORE: constructor injection
        this.llmClient = llmClient;
    }

    public String chat(String message) {
        return llmClient.generateReply(message); // CORE
    }
}
