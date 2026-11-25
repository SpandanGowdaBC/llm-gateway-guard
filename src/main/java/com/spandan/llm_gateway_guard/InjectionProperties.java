package com.spandan.llm_gateway_guard;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.injection")
public record InjectionProperties(
        int flagThreshold, // CORE: default 30
        int blockThreshold // CORE: default 70
) {
    public InjectionProperties {
        if (flagThreshold <= 0) flagThreshold = 30;
        if (blockThreshold <= 0) blockThreshold = 70;
    }
}
