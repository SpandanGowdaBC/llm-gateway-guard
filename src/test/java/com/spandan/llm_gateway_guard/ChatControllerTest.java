package com.spandan.llm_gateway_guard;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatControllerTest {

    private static final String SECRET = "local-dev-secret-key-that-is-at-least-32-bytes-long!";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ChatService chatService = new ChatService(new MockLlmClient());
        ChatController controller = new ChatController(chatService);
        JwtAuthFilter filter = new JwtAuthFilter(SECRET);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(filter)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private String createToken(String tenantId) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .claim("tenant_id", tenantId)
                .signWith(SECRET_KEY)
                .compact();
    }

    @Test
    void validRequest_returns200WithReplyAndTenant() throws Exception {
        String token = createToken("tenant-alpha");
        String json = "{\"message\": \"Hello LLM\"}";

        mockMvc.perform(post("/api/chat")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("Mock reply to: Hello LLM"))
                .andExpect(jsonPath("$.tenantId").value("tenant-alpha"));
    }

    @Test
    void blankMessage_returns400() throws Exception {
        String token = createToken("tenant-alpha");
        String json = "{\"message\": \"   \"}";

        mockMvc.perform(post("/api/chat")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("message must not be blank"));
    }

    @Test
    void missingMessageField_returns400() throws Exception {
        String token = createToken("tenant-alpha");
        String json = "{}";

        mockMvc.perform(post("/api/chat")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("message must not be blank"));
    }

    @Test
    void messageOver2000Characters_returns400() throws Exception {
        String token = createToken("tenant-alpha");
        String longMessage = "a".repeat(2001);
        String json = "{\"message\": \"" + longMessage + "\"}";

        mockMvc.perform(post("/api/chat")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("message must not exceed 2000 characters"));
    }

    @Test
    void noToken_returns401() throws Exception {
        String json = "{\"message\": \"Hello without token\"}";

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }
}
