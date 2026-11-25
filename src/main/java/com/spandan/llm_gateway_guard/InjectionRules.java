package com.spandan.llm_gateway_guard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.regex.Pattern;

@Configuration
public class InjectionRules {

    @Bean
    public InjectionRule ignoreInstructionsRule() {
        // CORE: catches commands trying to wipe system instructions (weight 50)
        Pattern pattern = Pattern.compile("\\b(ignore|disregard|forget)\\s+(?:(?:all|previous|prior|above|the)\\s+)*(instructions|rules|prompts|commands)\\b");
        return new InjectionRule("IGNORE_INSTRUCTIONS", pattern, 50);
    }

    @Bean
    public InjectionRule revealSystemPromptRule() {
        // CORE: catches extraction of hidden system instructions (weight 50)
        Pattern pattern = Pattern.compile("\\b(show|reveal|repeat|print|output|display)\\b.{0,30}\\b(system prompt|hidden prompt|initial prompt|secret instructions)\\b");
        return new InjectionRule("REVEAL_SYSTEM_PROMPT", pattern, 50);
    }

    @Bean
    public InjectionRule roleOverrideRule() {
        // CORE: catches jailbreak personas like developer mode or DAN (weight 40)
        Pattern pattern = Pattern.compile("\\b(you are now|act as|enable|enter)\\b.{0,20}\\b(unrestricted|developer mode|dan|jailbreak)\\b");
        return new InjectionRule("ROLE_OVERRIDE", pattern, 40);
    }

    @Bean
    public InjectionRule delimiterAbuseRule() {
        // CORE: catches forged model delimiter tags or headers (weight 45)
        Pattern pattern = Pattern.compile("(<\\|im_start\\|>|<\\|im_end\\|>|<\\|endoftext\\|>|###\\s*system\\b|\\[system\\])");
        return new InjectionRule("DELIMITER_ABUSE", pattern, 45);
    }
}
