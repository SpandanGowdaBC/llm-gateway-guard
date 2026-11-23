package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MockLlmClientTest {

    @Test
    void shouldGenerateMockReplyWithoutSpring() {
        LlmClient client = new MockLlmClient(); // CORE: pure Java, no Spring needed!
        String reply = client.generateReply("Hello world"); // CORE

        assertThat(reply).isEqualTo("Mock reply to: Hello world"); // CORE
    }
}
