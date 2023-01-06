package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOverTest {
    @Test
    public void twoPlayerGameOnePlayerStandsGameIsInProgress() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        final DeckFactory deckFactory = DeckFactory.createForTest(deck);
        Game game = new Game(2, new Shoe(deckFactory.decks()));
        game.initialDeal();

        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void threePlayerGameTwoPlayersStandGameIsInProgress() {
        Deck deck = MultiPlayerStubDeckFactory.threePlayersNotDealtBlackjack();
        final DeckFactory deckFactory = DeckFactory.createForTest(deck);
        Game game = new Game(3, new Shoe(deckFactory.decks()));
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOver() throws Exception {
        final DeckFactory deckFactory = DeckFactory.createForTest(StubDeckBuilder.playerCountOf(1)
                                                                       .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                                                       .buildWithDealerDealtBlackjack());
        Game game = new Game(1, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOverForTwoPlayers() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                       .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                       .buildWithDealerDealtBlackjack();
        final DeckFactory deckFactory = DeckFactory.createForTest(deck);
        Game game = new Game(2, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenPlayerHasBlackjackGameIsOver() {
        Deck deck = new StubDeck(Rank.TEN, Rank.JACK,
                                 Rank.ACE, Rank.EIGHT);
        final DeckFactory deckFactory = DeckFactory.createForTest(deck);
        Game game = new Game(1, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenTwoPlayersDealtBlackjackGameIsOver() throws Exception {
        final DeckFactory deckFactory = DeckFactory.createForTest(MultiPlayerStubDeckFactory
                                                                .twoPlayersAllDealtBlackjackDealerCouldHit());
        Game game = new Game(2, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();

    }
}