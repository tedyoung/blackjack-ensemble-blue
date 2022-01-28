package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class StubDeckBuilderTest {

    @Test
    public void createPlayerHitsDoesNotBustDeck() {
        StubDeckBuilder builder = new StubDeckBuilder();

        assertThat(builder.playerHitsOnceDoesNotBust().build())
                .isNotNull();
        assertThat(builder.build())
                .isEqualTo(SinglePlayerStubDeckFactory
                                   .createPlayerHitsDoesNotBustDeck()
                );
    }
}
