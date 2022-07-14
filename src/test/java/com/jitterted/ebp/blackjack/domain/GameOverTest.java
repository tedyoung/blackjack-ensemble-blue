package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOverTest {
    @Test
    public void twoPlayerGameOnePlayerStandsGameIsInProgress() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        Game game = new Game(deck, 2);
        game.initialDeal();

        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void threePlayerGameTwoPlayersStandGameIsInProgress() {
        Deck deck = MultiPlayerStubDeckFactory.threePlayersNotDealtBlackjack();
        Game game = new Game(deck, 3);
        game.initialDeal();

        game.playerStands();
        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOver() throws Exception {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                            .buildWithDealerDealtBlackjack(), 1);

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
        Game game = new Game(deck, 2);

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenPlayerHasBlackjackGameIsOver() {
        Deck deck = new StubDeck(Rank.TEN, Rank.JACK,
                                 Rank.ACE, Rank.EIGHT);
        Game game = new Game(deck, 1);

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }
}