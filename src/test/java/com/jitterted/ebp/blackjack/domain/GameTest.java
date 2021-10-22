package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void givenPlayerDealtBlackjackWhenPlayerHitsThenThrowsException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        Game game = new Game(playerDrawsBlackjackDeck);
        game.initialDeal();

        assertThatThrownBy(() -> {
            game.playerHits();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void givenPlayerDealtBlackjackWhenPlayerStandsThrowsException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        Game game = new Game(playerDrawsBlackjackDeck);
        game.initialDeal();

        assertThatThrownBy(() -> {
            game.playerStands();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void givenPlayerBustsWhenPlayerHitsThenThrowsException() throws Exception {
        Deck playerBustsDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE,
                                            Rank.TEN);
        Game game = new Game(playerBustsDeck);
        game.initialDeal();
        game.playerHits();

        assertThatThrownBy(() -> {
            game.playerHits();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void givenGameWithTwoPlayersWhenInitialDealThenEachPlayerHasTwoCards() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 2);

        game.initialDeal();

        assertThat(game.getPlayers())
                .extracting(Player::cards)
                .extracting(List::size)
                .containsExactly(2, 2);
    }

    @Test
    void givenGameAssignPlayerAnIdAndCheckIt() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 1);

        assertThat(game.currentPlayerId())
                .isEqualTo(0);
    }

    @Test
    public void givenMultiplePlayersEachPlayerGetsUniqueIdAssigned() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 2);

        assertThat(game.getPlayers())
                .extracting(Player::id)
                .containsExactly(0, 1);
    }

    @Test
    public void givenMultiplePlayersPlayerStandsWhenNextPlayerCommandThenSecondPlayerIsCurrent() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 2);
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    void givenMultiplePlayersFirstPlayerStandsThenDealerDoesNotTakeTheirTurn() {
        Deck deck = new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN,
                                 Rank.EIGHT, Rank.QUEEN, Rank.TWO, Rank.NINE);
        Game game = new Game(deck, 2);
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }
}
