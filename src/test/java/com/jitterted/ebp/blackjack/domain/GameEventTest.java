package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }


    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeck());

        game.initialDeal();

        assertThat(game.events())
                .containsExactly(new PlayerEvent(0, "Player has blackjack"));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerEvent(0, "Player stands"));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeck());
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerEvent(0, "Player busted"));
    }

    @Test
    public void multiplePlayersStandResultsInTwoStandEvents() throws Exception {
        Game game = new Game(MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack(), 2);
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        PlayerEvent player1event = new PlayerEvent(0, "Player stands");
        PlayerEvent player2event = new PlayerEvent(1, "Player stands");
        assertThat(game.events())
                .containsExactly(player1event, player2event);
    }

}