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
                .containsExactly(new PlayerEvent(66, "Player has blackjack"));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        Game game = new Game(StubDeck.createPlayerNotDealtBlackjackDeck());
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerEvent(66, "Player stands"));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        Game game = new Game(StubDeck.createPlayerHitsGoesBustDeck());
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerEvent(66, "Player busted"));
    }
}