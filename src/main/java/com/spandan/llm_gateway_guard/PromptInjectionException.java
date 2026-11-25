package com.spandan.llm_gateway_guard;

public class PromptInjectionException extends RuntimeException {
    public PromptInjectionException(String message) {
        super(message);
    }
}
