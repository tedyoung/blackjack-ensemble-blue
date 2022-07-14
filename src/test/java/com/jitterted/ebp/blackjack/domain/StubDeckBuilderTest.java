package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class StubDeckBuilderTest {

    /**
     * Full exercise of builder, shows how the deck is built internally
     */
    @Test
    public void createTwoPlayerAndDealerEachWithFourCards() {
        StubDeck stubDeck = StubDeckBuilder
                .playerCountOf(2)
                .addPlayerWithRanks(Rank.TWO, Rank.THREE, Rank.JACK, Rank.TWO)
                .addPlayerWithRanks(Rank.FIVE, Rank.SIX, Rank.FOUR, Rank.FIVE)
                .buildWithDealerRanks(Rank.FOUR, Rank.SEVEN, Rank.FOUR, Rank.KING);
        // @formatter:off
        assertThat(stubDeck)
                .isEqualTo(new StubDeck(// initial deal (2 cards for everyone)
                                        Rank.TWO,   Rank.FIVE,  Rank.FOUR,
                                        Rank.THREE, Rank.SIX,   Rank.SEVEN,
                                        // player 1's turn ("tail"), hits twice:
                                        Rank.JACK, Rank.TWO,
                                        // player 2's turn ("tail"), hits twice:
                                        Rank.FOUR, Rank.FIVE,
                                        // dealer gets the rest:
                                        Rank.FOUR, Rank.KING));
        // @formatter:on
        Game game = new Game(stubDeck, 2);
        game.initialDeal();

        // player 1 plays
        game.playerHits();
        game.playerHits();
        game.playerStands();

        // player 2 plays
        game.playerHits();
        game.playerHits();
        game.playerStands();

        assertThat(game.playerResults().get(0).cards())
                .extracting(Card::rank)
                .containsExactly(Rank.TWO, Rank.THREE, Rank.JACK, Rank.TWO);
        assertThat(game.playerResults().get(1).cards())
                .extracting(Card::rank)
                .containsExactly(Rank.FIVE, Rank.SIX, Rank.FOUR, Rank.FIVE);
        assertThat(game.dealerHand().cards())
                .extracting(Card::rank)
                .containsExactly(Rank.FOUR, Rank.SEVEN, Rank.FOUR, Rank.KING);
    }

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
                                                         .addPlayerDealtBlackjack();

        assertThatThrownBy(stubDeckBuilder::buildWithDealerDoesNotDrawCards)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Player count mismatch");
    }

    @Test
    public void addsFewerPlayersThanSpecified() {
        StubDeckBuilder stubDeckBuilder = StubDeckBuilder.playerCountOf(2)
                                                         .addPlayerHitsAndGoesBust();

        assertThatThrownBy(stubDeckBuilder::buildWithDealerDoesNotDrawCards)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Player count mismatch");
    }
}
