package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MultiPlayerGameTurnTest {

    @Test
    public void skipPastPlayerInMiddleWhoHasBlackjack() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(3)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameFactory.createMultiPlayerGamePlaceBetsInitialDeal(3, deck);

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(2);
    }

    @Test
    void skipPastFirstPlayerWhoHasBlackjack() {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameFactory.createTwoPlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void skipPastTwoPlayersHavingBlackjack() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(4)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();

        Game game = GameFactory.createMultiPlayerGamePlaceBetsInitialDeal(4, deck);

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(3);
    }
}