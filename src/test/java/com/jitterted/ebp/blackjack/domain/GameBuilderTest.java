package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameBuilderTest {

    @Test
    void numberOfPlayersMustMatchPlayerCount() {
        GameBuilder gameBuilder = GameBuilder.playerCountOf(1)
                                             .addPlayer(new PlayerId(32))
                                             .addPlayer(new PlayerId(21))
                                             .placeDefaultBets();
        assertThatThrownBy(gameBuilder::build)
                .isExactlyInstanceOf(PlayerCountMismatch.class)
                .hasMessage("PlayerCount is 1, but 2 players were added.");
    }
}