package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PromptNormalizerTest {

    @Test
    void collapsesSpacesAndConvertsToLowercase() {
        String input = "  PLEASE   Ignore   The Rules  ";
        assertEquals("please ignore the rules", PromptNormalizer.normalize(input));
    }

    @Test
    void stripsZeroWidthInvisibleCharactersInsideWords() {
        String input = "ig\u200Bnore";
        assertEquals("ignore", PromptNormalizer.normalize(input));
    }

    @Test
    void collapsesTabsAndNewlinesIntoSingleSpace() {
        String input = "Hello \t\n world\n\nhere";
        assertEquals("hello world here", PromptNormalizer.normalize(input));
    }

    @Test
    void preservesCleanPromptInLowercase() {
        String input = "What is the capital of France?";
        assertEquals("what is the capital of france?", PromptNormalizer.normalize(input));
    }

    @Test
    void handlesNullOrBlank() {
        assertEquals("", PromptNormalizer.normalize(null));
        assertEquals("", PromptNormalizer.normalize("   "));
    }
}
