package com.spandan.llm_gateway_guard;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WhoAmIControllerTest {

    private static final String SECRET = "local-dev-secret-key-that-is-at-least-32-bytes-long!";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final SecretKey WRONG_KEY = Keys.hmacShaKeyFor("different-key-that-is-at-least-32-bytes-long!".getBytes(StandardCharsets.UTF_8));

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        JwtAuthFilter filter = new JwtAuthFilter(SECRET);
        mockMvc = MockMvcBuilders.standaloneSetup(new WhoAmIController())
                .addFilters(filter)
                .build();
    }

    private String createToken(String tenantId, long offsetMs, SecretKey key) {
        var builder = Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + offsetMs))
                .signWith(key);
        if (tenantId != null) {
            builder.claim("tenant_id", tenantId);
        }
        return builder.compact();
    }

    @Test
    void validToken_returns200AndTenant() throws Exception {
        String token = createToken("acme-corp", 60_000, SECRET_KEY);

        mockMvc.perform(get("/api/whoami").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenant_id").value("acme-corp"));
    }

    @Test
    void missingHeader_returns401() throws Exception {
        mockMvc.perform(get("/api/whoami"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Missing or invalid Authorization header"));
    }

    @Test
    void malformedToken_returns401() throws Exception {
        mockMvc.perform(get("/api/whoami").header("Authorization", "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Invalid token signature or format"));
    }

    @Test
    void badSignature_returns401() throws Exception {
        String token = createToken("acme-corp", 60_000, WRONG_KEY);

        mockMvc.perform(get("/api/whoami").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Invalid token signature or format"));
    }

    @Test
    void expiredToken_returns401() throws Exception {
        String token = createToken("acme-corp", -60_000, SECRET_KEY);

        mockMvc.perform(get("/api/whoami").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Token expired"));
    }

    @Test
    void missingTenantIdClaim_returns401() throws Exception {
        String token = createToken(null, 60_000, SECRET_KEY);

        mockMvc.perform(get("/api/whoami").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Missing tenant_id claim"));
    }

    @Test
    void threadLocal_isEmptyAfterRequest() throws Exception {
        String token = createToken("acme-corp", 60_000, SECRET_KEY);

        mockMvc.perform(get("/api/whoami").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        assertThat(TenantContext.getTenantId()).isNull();
    }
}
