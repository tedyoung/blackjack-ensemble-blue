package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameOverTest {
    @Test
    public void twoPlayerGameOnePlayerStandsGameIsInProgress() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        final List<Deck> deckFactory = List.of(deck);
        Game game = new Game(PlayerCount.of(2), new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void threePlayerGameTwoPlayersStandGameIsInProgress() {
        Deck deck = MultiPlayerStubDeckFactory.threePlayersNotDealtBlackjack();
        final List<Deck> deckFactory = List.of(deck);
        Game game = new Game(PlayerCount.of(3), new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOver() throws Exception {
        final List<Deck> deckFactory = List.of(StubDeckBuilder.playerCountOf(1)
                                                              .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                                              .buildWithDealerDealtBlackjack());
        Game game = new Game(PlayerCount.of(1), new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOverForTwoPlayers() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                   .buildWithDealerDealtBlackjack();
        final List<Deck> deckFactory = List.of(deck);
        Game game = new Game(PlayerCount.of(2), new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenPlayerHasBlackjackGameIsOver() {
        Deck deck = new StubDeck(Rank.TEN, Rank.JACK,
                                 Rank.ACE, Rank.EIGHT);
        final List<Deck> deckFactory = List.of(deck);
        Game game = new Game(PlayerCount.of(1), new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenTwoPlayersDealtBlackjackGameIsOver() throws Exception {
        final List<Deck> deckFactory = List.of(MultiPlayerStubDeckFactory
                                                       .twoPlayersAllDealtBlackjackDealerCouldHit());
        Game game = new Game(PlayerCount.of(2), new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();

    }
}