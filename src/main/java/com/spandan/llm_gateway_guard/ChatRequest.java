package com.spandan.llm_gateway_guard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "message must not be blank") // CORE
        @Size(max = 2000, message = "message must not exceed 2000 characters") // CORE
        String message
) {}
