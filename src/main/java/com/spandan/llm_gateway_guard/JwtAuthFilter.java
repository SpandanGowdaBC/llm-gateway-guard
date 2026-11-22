package com.spandan.llm_gateway_guard;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;

    public JwtAuthFilter(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/"); // CORE: only guard /api/**
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, "Missing or invalid Authorization header"); // CORE
            return;
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload(); // CORE
            String tenantId = claims.get("tenant_id", String.class);
            if (tenantId == null || tenantId.isBlank()) {
                sendError(response, "Missing tenant_id claim"); // CORE
                return;
            }

            try {
                TenantContext.setTenantId(tenantId); // CORE: set current tenant
                filterChain.doFilter(request, response); // CORE: forward down the chain
            } finally {
                TenantContext.clear(); // CORE: ALWAYS cleared, preventing tenant bleed
            }
        } catch (ExpiredJwtException e) {
            sendError(response, "Token expired"); // CORE
        } catch (JwtException | IllegalArgumentException e) {
            sendError(response, "Invalid token signature or format"); // CORE
        }
    }

    private void sendError(HttpServletResponse res, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // CORE: 401
        res.setContentType("application/json");
        res.getWriter().write(String.format("{\"error\":\"UNAUTHORIZED\",\"message\":\"%s\"}", message));
    }
}
