package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        Game game = GameBuilder.createOnePlayerGamePlaceBets(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }

    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory
                .createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck, PlayerId.of(33));

        game.initialDeal();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(33, PlayerReasonDone.PLAYER_HAS_BLACKJACK));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck();
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck, PlayerId.of(111));
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(111, PlayerReasonDone.PLAYER_STANDS));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck, PlayerId.of(75));
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(75, PlayerReasonDone.PLAYER_BUSTED));
    }

    @Test
    void multiplePlayersStandResultsInTwoStandEvents() throws Exception {
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(
                MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack(),
                PlayerId.of(81),
                PlayerId.of(34));

        game.playerStands();
        game.playerStands();

        PlayerDoneEvent player1event = new PlayerDoneEvent(81, PlayerReasonDone.PLAYER_STANDS);
        PlayerDoneEvent player2event = new PlayerDoneEvent(34, PlayerReasonDone.PLAYER_STANDS);
        assertThat(game.events())
                .containsExactly(player1event, player2event);
    }

}
