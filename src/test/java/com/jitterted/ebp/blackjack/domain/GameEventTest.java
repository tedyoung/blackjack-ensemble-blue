package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        Game game = new Game(StubDeck.createPlayerNotDealtBlackjackDeck());

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }

    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        Game game = new Game(StubDeck.createPlayerDealtBlackjackDeck());

        game.initialDeal();

        assertThat(game.events())
                .hasSize(1);
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        Game game = new Game(StubDeck.createPlayerNotDealtBlackjackDeck());
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .hasSize(1);
    }
}