package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOverTest {
    @Test
    public void twoPlayerGameOnePlayerStandsGameIsInProgress() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck);

        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void threePlayerGameTwoPlayersStandGameIsInProgress() {
        Deck deck = MultiPlayerStubDeckFactory.threePlayersNotDealtBlackjack();
        Game game = GameBuilder.playerCountOf(3)
                               .deck(deck)
                               .withDefaultPlayers()
                               .placeDefaultBets()
                               .initialDeal()
                               .build();
        game.playerStands();
        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOver() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .buildWithDealerDealtBlackjack();
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck);

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
        Game game = GameBuilder.createTwoPlayerGamePlaceBets(deck, new PlayerId(44), new PlayerId(17));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenPlayerHasBlackjackGameIsOver() {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerDealtBlackjack()
                                   .buildWithDealerRanks(Rank.JACK, Rank.EIGHT);
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
    }

    @Test
    public void whenTwoPlayersDealtBlackjackGameIsOver() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory
                .twoPlayersAllDealtBlackjackDealerCouldHit();
        Game game = GameBuilder.createTwoPlayerGamePlaceBets(deck, new PlayerId(44), new PlayerId(17));

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();

    }
}