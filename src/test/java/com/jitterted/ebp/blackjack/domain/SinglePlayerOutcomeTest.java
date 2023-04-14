package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SinglePlayerOutcomeTest {

    @Test
    public void playerGoesBustResultsInPlayerLoses() throws Exception {
        Deck playerHitsGoesBustDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                                   Rank.TEN, Rank.JACK,
                                                   Rank.THREE);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(playerHitsGoesBustDeck);

        game.playerHits();

        assertThat(game.currentPlayerOutcome())
                .isEqualByComparingTo(PlayerOutcome.PLAYER_BUSTED);
    }

    @Test
    public void playerBeatsDealer() throws Exception {
        Deck playerBeatsDealerDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                                  Rank.TEN, Rank.JACK);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(playerBeatsDealerDeck);

        game.playerStands();

        assertThat(game.currentPlayerOutcome())
                .isEqualByComparingTo(PlayerOutcome.PLAYER_BEATS_DEALER);
    }

    @Test
    public void dealerDrawsAdditionalCardAfterPlayerStands() throws Exception {
        Deck dealerDrawsAdditionalCardDeck =
                StubDeckBuilder.playerCountOf(1)
                               .addPlayerWithRanks(Rank.TEN, Rank.EIGHT)
                               .buildWithDealerRanks(Rank.QUEEN, Rank.FIVE, Rank.SIX);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(dealerDrawsAdditionalCardDeck);

        game.playerStands();

        assertThat(game.dealerHand().cards())
                .hasSize(3);
    }

    @Test
    public void playerDrawsBlackjack() throws Exception {
        Deck playerDrawsBlackjackDeck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBets(playerDrawsBlackjackDeck);

        game.initialDeal();

        assertThat(game.currentPlayerOutcome())
                .isEqualByComparingTo(PlayerOutcome.BLACKJACK);
    }

    @Test
    public void playerDraws21WithThreeCardsButIsNotBlackjack() throws Exception {
        Deck deck = new StubDeck(Rank.SEVEN, Rank.TEN,
                                 Rank.SEVEN, Rank.EIGHT,
                                 Rank.SEVEN);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(deck);

        game.playerHits();
        game.playerStands();

        assertThat(game.currentPlayerOutcome())
                .isNotEqualByComparingTo(PlayerOutcome.BLACKJACK);
    }


    @Test
    void gameIsNotOverGameOutcomeThrowsException() {
        Deck deck = new StubDeck(Rank.TEN, Rank.FIVE,
                                 Rank.EIGHT, Rank.TWO);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(deck);

        assertThatThrownBy(game::currentPlayerOutcome)
                .isInstanceOf(IllegalStateException.class);

    }
}
