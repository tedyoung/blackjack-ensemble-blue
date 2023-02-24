package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class PlayerCountTest {

    @Test
    void cannotHaveZeroPlayers() {
        assertThatThrownBy(() -> new PlayerCount(0))
                .isInstanceOf(NotEnoughPlayers.class);
    }

    @Test
    void cannotHaveMoreThanFivePlayers() {
        assertThatThrownBy(() -> new PlayerCount(6))
                .isInstanceOf(TooManyPlayers.class);
    }

    @ParameterizedTest()
    @ValueSource(ints = { 1, 2, 3, 4, 5})
    void allowedNumberOfPlayersDoesNotThrowException(int numberOfPlayers) {
        assertThat(new PlayerCount(numberOfPlayers).playerCount())
                .isEqualTo(numberOfPlayers);
    }
}