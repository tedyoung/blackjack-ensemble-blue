package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealPlayerNotDealtBlackjackResultsInNoEvents() {
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());
        Game game = new Game(1, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }


    @Test
    void initialDealPlayerDealtBlackjackResultsInBlackjackEvent() {
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());
        Game game = new Game(1, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_HAS_BLACKJACK));
    }

    @Test
    void firstPlayerStandsResultsInStandEvent() {
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerNotDealtBlackjackDeck());
        Game game = new Game(1, new Shoe(deckFactory.decks()));
        game.initialDeal();

        game.playerStands();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_STANDS));
    }

    @Test
    void firstPlayerHitsAndGoesBustResultsInPlayerBustEvent() {
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit());
        Game game = new Game(1, new Shoe(deckFactory.decks()));
        game.initialDeal();

        game.playerHits();

        assertThat(game.events())
                .containsExactly(new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_BUSTED));
    }

    @Test
    public void multiplePlayersStandResultsInTwoStandEvents() throws Exception {
        final DeckFactory deckFactory = DeckFactory.createForTest(MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack());
        Game game = new Game(2, new Shoe(deckFactory.decks()));
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        PlayerDoneEvent player1event = new PlayerDoneEvent(0, PlayerReasonDone.PLAYER_STANDS);
        PlayerDoneEvent player2event = new PlayerDoneEvent(1, PlayerReasonDone.PLAYER_STANDS);
        assertThat(game.events())
                .containsExactly(player1event, player2event);
    }

}