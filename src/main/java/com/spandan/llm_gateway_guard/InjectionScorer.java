package com.spandan.llm_gateway_guard;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class InjectionScorer {

    private final List<InjectionRule> rules;
    private final InjectionProperties properties;

    public InjectionScorer(List<InjectionRule> rules, InjectionProperties properties) {
        this.rules = rules;
        this.properties = properties;
    }

    public InjectionResult score(String text) {
        String normalized = PromptNormalizer.normalize(text);
        int totalScore = 0;
        List<String> matched = new ArrayList<>();

        for (InjectionRule rule : rules) {
            if (rule.pattern().matcher(normalized).find()) {
                totalScore += rule.weight();
                matched.add(rule.name());
            }
        }

        int score = Math.min(100, totalScore);
        Decision decision;
        if (score >= properties.blockThreshold()) {
            decision = Decision.BLOCK;
        } else if (score >= properties.flagThreshold()) {
            decision = Decision.FLAG;
        } else {
            decision = Decision.ALLOW;
        }

        return new InjectionResult(score, List.copyOf(matched), decision);
    }
}
