package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        final List<Deck> deckFactory = List.of(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());
        Game game = new Game(new PlayerCount(1), new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }


    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        final List<Deck> deckFactory = List.of(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());
        Game game = new Game(new PlayerCount(1), new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_HAS_BLACKJACK));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        final List<Deck> deckFactory = List.of(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());
        Game game = new Game(new PlayerCount(1), new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_STANDS));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        final List<Deck> deckFactory = List.of(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit());
        Game game = new Game(new PlayerCount(1), new Shoe(deckFactory));
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_BUSTED));
    }

    @Test
    public void multiplePlayersStandResultsInTwoStandEvents() throws Exception {
        final List<Deck> deckFactory = List.of(MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack());
        Game game = new Game(new PlayerCount(2), new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        PlayerDoneEvent player1event = new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_STANDS);
        PlayerDoneEvent player2event = new PlayerDoneEvent(1, PlayerReasonDone.PLAYER_STANDS);
        assertThat(game.events())
                .containsExactly(player1event, player2event);
    }

}