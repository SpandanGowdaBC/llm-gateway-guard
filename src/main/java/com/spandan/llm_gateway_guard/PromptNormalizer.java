package com.spandan.llm_gateway_guard;

import java.util.Locale;
import java.util.regex.Pattern;

public final class PromptNormalizer {

    private static final Pattern INVISIBLE_CHARS = Pattern.compile("[\\u200B-\\u200D\\uFEFF]|[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    private PromptNormalizer() {}

    public static String normalize(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }
        // CORE: strip zero-width characters and invisible control bytes
        String stripped = INVISIBLE_CHARS.matcher(input).replaceAll("");
        // CORE: collapse multiple spaces/newlines into a single space and trim
        String collapsed = WHITESPACE.matcher(stripped).replaceAll(" ").trim();
        // CORE: lowercase for consistent case-insensitive regex matching
        return collapsed.toLowerCase(Locale.ROOT);
    }
}
