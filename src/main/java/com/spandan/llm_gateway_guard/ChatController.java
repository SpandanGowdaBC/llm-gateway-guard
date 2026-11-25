package com.spandan.llm_gateway_guard;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // CORE
@RequestMapping("/api") // CORE: protected under /api/**
public class ChatController {

    private final ChatService chatService; // CORE

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat") // CORE
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        // CORE: delegate to ChatService which redacts and invokes the LLM
        return chatService.chat(request.message());
    }
}
