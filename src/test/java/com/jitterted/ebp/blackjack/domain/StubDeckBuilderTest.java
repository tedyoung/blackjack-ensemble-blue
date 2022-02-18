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
                                   .buildWithDealerDoesNotDrawCards();

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
                                   .buildWithDealerDoesNotDrawCards();

        assertThat(stubDeck)
                .isEqualTo(new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                        Rank.ACE, Rank.TEN)
                );
    }

    @Test
    public void createTwoPlayerBlackjackAndHitsDoesNotBust() {
        StubDeck stubDeck = StubDeckBuilder.playerCountOf(2)
                                           .addPlayerDealtBlackjack()
                                           .addPlayerHitsOnceDoesNotBust()
                                           .withDealerDoesNotDrawCards()
                                           .buildWithDealerDoesNotDrawCards();
        // @formatter:off
        assertThat(stubDeck)
                .isEqualTo(new StubDeck(Rank.QUEEN, Rank.QUEEN, Rank.EIGHT,
                                        Rank.ACE,   Rank.SEVEN, Rank.TEN,
                                                    Rank.THREE));
        // @formatter:on
    }

    @Test
    public void createOnePlayerHitsAndGoesBust() {
        StubDeck stubDeck = StubDeckBuilder.playerCountOf(1)
                                           .addPlayerHitsAndGoesBust()
                                           .withDealerDoesNotDrawCards()
                                           .buildWithDealerDoesNotDrawCards();
        // @formatter:off
        assertThat(stubDeck)
                .isEqualTo(new StubDeck(Rank.JACK, Rank.EIGHT,
                                        Rank.NINE, Rank.TEN,
                                        Rank.FOUR));
        // @formatter:on
    }

    @Test
    public void addsMorePlayersThanSpecified() {
        StubDeckBuilder stubDeckBuilder = StubDeckBuilder.playerCountOf(1)
                                                         .addPlayerHitsAndGoesBust()
                                                         .addPlayerDealtBlackjack()
                                                         .withDealerDoesNotDrawCards();

        assertThatThrownBy(stubDeckBuilder::buildWithDealerDoesNotDrawCards)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Player count mismatch");
    }

    @Test
    public void addsFewerPlayersThanSpecified() {
        StubDeckBuilder stubDeckBuilder = StubDeckBuilder.playerCountOf(2)
                                                         .addPlayerHitsAndGoesBust()
                                                         .withDealerDoesNotDrawCards();

        assertThatThrownBy(stubDeckBuilder::buildWithDealerDoesNotDrawCards)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Player count mismatch");
    }
}
