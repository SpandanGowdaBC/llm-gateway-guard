package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PingController.class) // CORE
class PingControllerTest {

    @Autowired // CORE
    private MockMvc mockMvc;

    @Test
    void shouldReturnGatewayIsAlive() throws Exception {
        mockMvc.perform(get("/ping")) // CORE
                .andExpect(status().isOk()) // CORE
                .andExpect(content().string("gateway is alive")); // CORE
    }
}
