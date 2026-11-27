package com.spandan.llm_gateway_guard;

public final class TokenEstimator {

    private TokenEstimator() {}

    public static int estimate(String text) {
        if (text == null || text.isEmpty()) {
            return 0; // CORE: null or empty text costs 0 tokens
        }
        // CORE: calculate ceil(length / 4.0) ensuring at least 1 token for non-empty text
        return (int) Math.ceil(text.length() / 4.0);
    }
}
