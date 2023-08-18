package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        Game game = GameFactory.createOnePlayerGamePlaceBets(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }

    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory
                .createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck, new PlayerId(33));

        game.initialDeal();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(33, PlayerReasonDone.PLAYER_HAS_BLACKJACK));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck();
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck, new PlayerId(111));
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(111, PlayerReasonDone.PLAYER_STANDS));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBets(deck, new PlayerId(75));
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(75, PlayerReasonDone.PLAYER_BUSTED));
    }

    @Test
    public void multiplePlayersStandResultsInTwoStandEvents() throws Exception {
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(
                MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack());

        game.playerStands();
        game.playerStands();

        PlayerDoneEvent player1event = new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_STANDS);
        PlayerDoneEvent player2event = new PlayerDoneEvent(1, PlayerReasonDone.PLAYER_STANDS);
        assertThat(game.events())
                .containsExactly(player1event, player2event);
    }

}
