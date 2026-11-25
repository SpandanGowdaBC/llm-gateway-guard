package com.spandan.llm_gateway_guard;

import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Component
public class PiiRedactor {

    private final List<RedactionRule> rules;

    public PiiRedactor(List<RedactionRule> rules) {
        // CORE: Spring injects beans in order of @Order annotation
        this.rules = rules;
    }

    public RedactionResult redact(String text) {
        if (text == null || text.isBlank()) {
            return new RedactionResult(text, Collections.emptyMap());
        }

        String currentText = text;
        Map<String, Integer> counts = new LinkedHashMap<>();

        for (RedactionRule rule : rules) {
            Matcher matcher = rule.pattern().matcher(currentText);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            if (count > 0) {
                // CORE: replace matches and tally count for this rule
                currentText = matcher.replaceAll(rule.placeholder());
                counts.put(rule.name(), count);
            }
        }

        return new RedactionResult(currentText, Collections.unmodifiableMap(counts));
    }
}
