package com.spandan.llm_gateway_guard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenEstimatorTest {

    @Test
    void shortText_roundsUpToTwoTokens() {
        // "hello" has 5 characters: 5 / 4.0 = 1.25 -> rounds up to 2
        assertEquals(2, TokenEstimator.estimate("hello"));
    }

    @Test
    void longText_roundsUpToThreeTokens() {
        // "hello world" has 11 characters: 11 / 4.0 = 2.75 -> rounds up to 3
        assertEquals(3, TokenEstimator.estimate("hello world"));
    }

    @Test
    void emptyText_returnsZero() {
        assertEquals(0, TokenEstimator.estimate(""));
    }

    @Test
    void nullText_returnsZero() {
        assertEquals(0, TokenEstimator.estimate(null));
    }

    @Test
    void singleCharacter_returnsAtLeastOneToken() {
        // 1 character: 1 / 4.0 = 0.25 -> rounds up to 1
        assertEquals(1, TokenEstimator.estimate("a"));
    }
}
