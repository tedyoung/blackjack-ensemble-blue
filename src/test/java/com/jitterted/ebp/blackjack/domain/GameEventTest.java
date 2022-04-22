package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck(), 1);

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }


    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);

        game.initialDeal();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, "Player has blackjack"));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck(), 1);
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, "Player stands"));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit(), 1);
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, "Player busted"));
    }

    @Test
    public void multiplePlayersStandResultsInTwoStandEvents() throws Exception {
        Game game = new Game(MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack(), 2);
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        PlayerDoneEvent player1event = new PlayerDoneEvent(0, "Player stands");
        PlayerDoneEvent player2event = new PlayerDoneEvent(1, "Player stands");
        assertThat(game.events())
                .containsExactly(player1event, player2event);
    }

}