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

    public ChatController(ChatService chatService) { // CORE: constructor injection
        this.chatService = chatService;
    }

    @PostMapping("/chat") // CORE
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) { // CORE
        String reply = chatService.chat(request.message()); // CORE
        String tenantId = TenantContext.getTenantId(); // CORE: read from Day 1 context
        return new ChatResponse(reply, tenantId); // CORE
    }
}
