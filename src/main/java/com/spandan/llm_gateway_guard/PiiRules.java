package com.spandan.llm_gateway_guard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import java.util.regex.Pattern;

@Configuration
public class PiiRules {

    @Bean
    @Order(1)
    public RedactionRule apiKeyRule() {
        // CORE: API_KEY rule runs first (Order 1) to protect secret keys
        Pattern apiKeyPattern = Pattern.compile("\\bsk-[a-zA-Z0-9_-]{20,}\\b");
        return new RedactionRule("API_KEY", apiKeyPattern, "[API_KEY_REDACTED]");
    }

    @Bean
    @Order(2)
    public RedactionRule emailRule() {
        // CORE: compile regex once and package into a RedactionRule
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        return new RedactionRule("EMAIL", emailPattern, "[EMAIL_REDACTED]");
    }

    @Bean
    @Order(3)
    public RedactionRule phoneRule() {
        // CORE: linear-time regex matching Indian and international phone formats
        Pattern phonePattern = Pattern.compile("(?:(?:\\+\\d{1,3}[- ]?)|\\b)(?:[6-9]\\d{4}[- ]?\\d{5}|\\d{3}[- ]\\d{3}[- ]\\d{4})\\b");
        return new RedactionRule("PHONE", phonePattern, "[PHONE_REDACTED]");
    }
}
