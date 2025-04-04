package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class BetTest {
    
    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 101})
    void doNotAllowInvalidBetAmounts(int invalidBetAmount) {
        assertThatThrownBy(() -> Bet.of(invalidBetAmount))
                .isExactlyInstanceOf(InvalidBetAmount.class)
                .hasMessage("Bet amount: %d is not within 1 to 100", invalidBetAmount);
    }

    @ParameterizedTest
    @ValueSource(ints = {0})
    void validationResultForInvalidBetAmountIsInvalid(int betAmount) {
        assertThat(Bet.isValidAmount(betAmount))
                .isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 99})
    void validationResultForValidBetAmountIsValid(int betAmount) {
        assertThat(Bet.isValidAmount(betAmount))
                .isTrue();
    }
}