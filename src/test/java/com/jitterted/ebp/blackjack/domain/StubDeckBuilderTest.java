package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class StubDeckBuilderTest {

//          StubDeckBuilder.playerCountOf(1)
//              .addPlayerHitsOnceDoesNotBust()             // player 1
//              .withDealerDoesNotDrawCards()               // dealer
//              .build();

    @Test
    public void createSinglePlayerHitsDoesNotBust() {
        StubDeckBuilder builder = StubDeckBuilder.playerCountOf(1);
        StubDeck stubDeck = builder.addPlayerHitsOnceDoesNotBust()
                                   .withDealerDoesNotDrawCards()
                                   .build();

        assertThat(stubDeck)
                .isEqualTo(new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                        Rank.SEVEN, Rank.TEN,
                                        Rank.THREE)
                );
    }

    @Test
    public void createSinglePlayerDealtBlackjack() {
        StubDeckBuilder builder = StubDeckBuilder.playerCountOf(1);
        StubDeck stubDeck = builder.addPlayerDealtBlackjack()
                                   .withDealerDoesNotDrawCards()
                                   .build();

        assertThat(stubDeck)
                .isEqualTo(new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                        Rank.ACE, Rank.TEN)
                );
    }

    @Test
    public void createTwoPlayerBlackjackAndHitsDoesNotBust() throws Exception {
        StubDeck stubDeck = StubDeckBuilder.playerCountOf(2)
                                           .addPlayerDealtBlackjack()
                                           .addPlayerHitsOnceDoesNotBust()
                                           .withDealerDoesNotDrawCards()
                                           .build();
        // @formatter:off
        assertThat(stubDeck)
                .isEqualTo(new StubDeck(Rank.QUEEN, Rank.QUEEN, Rank.EIGHT,
                                        Rank.ACE,   Rank.SEVEN, Rank.TEN,
                                                    Rank.THREE));
        // @formatter:on
    }

    @Test
    public void createThreePlayerVariety() throws Exception {
        StubDeck stubDeck = StubDeckBuilder.playerCountOf(1)
                                           .addPlayerHitsAndGoesBust()
                                           .withDealerDoesNotDrawCards()
                                           .build();
        // @formatter:off
        assertThat(stubDeck)
                .isEqualTo(new StubDeck(Rank.JACK, Rank.EIGHT,
                                        Rank.NINE, Rank.TEN,
                                        Rank.FOUR));
        // @formatter:on
    }
}
