package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SinglePlayerOutcomeTest {

    @Test
    public void playerGoesBustResultsInPlayerLoses() throws Exception {
        Deck playerHitsGoesBustDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                                   Rank.TEN, Rank.FOUR,
                                                   Rank.THREE);
        Game game = new Game(playerHitsGoesBustDeck);

        game.initialDeal();

        game.playerHits();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(PlayerOutcome.PLAYER_BUSTED);
    }

    @Test
    public void playerBeatsDealer() throws Exception {
        Deck playerBeatsDealerDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                                  Rank.TEN, Rank.JACK);
        Game game = new Game(playerBeatsDealerDeck);
        game.initialDeal();

        game.playerStands();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(PlayerOutcome.PLAYER_BEATS_DEALER);
    }

    @Test
    public void dealerDrawsAdditionalCardAfterPlayerStands() throws Exception {
        Deck dealerDrawsAdditionalCardDeck = new StubDeck(Rank.TEN, Rank.QUEEN,
                                                          Rank.EIGHT, Rank.FIVE,
                                                          Rank.SIX);
        Game game = new Game(dealerDrawsAdditionalCardDeck);
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards())
                .hasSize(3);
    }

    @Test
    public void playerDrawsBlackjack() throws Exception {
        Deck playerDrawsBlackjackDeck = StubDeck.createBlackjackDeck();

        Game game = new Game(playerDrawsBlackjackDeck);
        game.initialDeal();

        assertThat(game.determineOutcome())
                .isEqualByComparingTo(PlayerOutcome.BLACKJACK);
    }

    @Test
    public void playerDraws21WithThreeCardsButIsNotBlackjack() throws Exception {
        Deck deck = new StubDeck(Rank.SEVEN, Rank.TEN,
                                 Rank.SEVEN, Rank.EIGHT,
                                 Rank.SEVEN);
        Game game = new Game(deck);
        game.initialDeal();
        game.playerHits();
        game.playerStands();

        assertThat(game.determineOutcome())
                .isNotEqualByComparingTo(PlayerOutcome.BLACKJACK);
    }

    @Test
    public void gameIsOverWhenPlayerHasBlackjack() throws Exception {
        Deck deck = new StubDeck(Rank.TEN, Rank.FIVE,
                                 Rank.ACE, Rank.TWO);
        Game game = new Game(deck);
        game.initialDeal();

        assertThat(game.isPlayerDone())
                .isTrue();
    }

    @Test
    void gameIsNotOverGameOutcomeThrowsException() {
        Deck deck = new StubDeck(Rank.TEN, Rank.FIVE,
                                 Rank.EIGHT, Rank.TWO);
        Game game = new Game(deck);
        game.initialDeal();

        assertThatThrownBy(() -> {
            game.determineOutcome();
        }).isInstanceOf(IllegalStateException.class);

    }
}
