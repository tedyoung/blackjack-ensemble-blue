package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PlayerCountTest {

    @Test
    void cannotHaveZeroPlayers() {
        assertThatThrownBy(() -> PlayerCount.of(0))
                .isInstanceOf(NotEnoughPlayers.class);
    }

    @Test
    void cannotHaveMoreThanFivePlayers() {
        assertThatThrownBy(() -> PlayerCount.of(6))
                .isInstanceOf(TooManyPlayers.class);
    }

    @ParameterizedTest()
    @ValueSource(ints = { 1, 2, 3, 4, 5})
    void allowedNumberOfPlayersDoesNotThrowException(int numberOfPlayers) {
        assertThat(PlayerCount.of(numberOfPlayers).playerCount())
                .isEqualTo(numberOfPlayers);
    }
}