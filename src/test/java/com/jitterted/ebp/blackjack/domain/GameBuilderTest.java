package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameBuilderTest {

    @Test
    void gameBuilderPlayerCountLimitsNumberOfPlayers() {
        GameBuilder.playerCountOf(1)
                          .addPlayer(new PlayerId(32))
                          .addPlayer(new PlayerId(21))
                          .placeBets()
                          .build();

    }
}