package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.Result;
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
    @ValueSource(ints = {-1, 0, 101})
    void validationResultForInvalidBetAmountIsInvalid(int betAmount) {
        assertThat(Bet.isValid(betAmount))
                .isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 99})
    void validationResultForValidBetAmountIsValid(int betAmount) {
        assertThat(Bet.isValid(betAmount))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 101})
    void validationResultForInvalidBetAmountIsFailureResult(int betAmount) {
        Result<Bet> result = Bet.validate(betAmount);
        assertThat(result.isFailure())
                .isTrue();
        assertThat(result.failureMessages())
                .containsExactly("Bet amount: %d is not within 1 to 100".formatted(betAmount));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 99})
    void validationResultForValidBetAmountIsSuccessResult(int betAmount) {
        assertThat(Bet.validate(betAmount).isSuccess())
                .isTrue();
    }
}