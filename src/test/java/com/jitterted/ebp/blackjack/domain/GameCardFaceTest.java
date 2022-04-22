package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameCardFaceTest {

    @Test
    public void gameWhereDealerSecondCardIsFaceDownAfterInitialDeal() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);

        game.initialDeal();

        assertThat(game.dealerHand().cards().get(0).isFaceDown())
                .isFalse();
        assertThat(game.dealerHand().cards().get(1).isFaceDown())
                .isTrue();
    }

    @Test
    public void playerCardsAreAlwaysFaceUpAfterInitialDeal() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);

        game.initialDeal();

        assertThat(game.currentPlayerCards().get(0).isFaceDown())
                .isFalse();
        assertThat(game.currentPlayerCards().get(1).isFaceDown())
                .isFalse();
    }

    @Test
    public void whenPlayerStandsDealerSecondCardIsFaceUpAfterDealerTurn() {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerWithRanks(Rank.TEN, Rank.JACK)
                                            .buildWithDealerDoesNotDrawCards(), 1);
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards().get(0).isFaceDown())
                .isFalse();
        assertThat(game.dealerHand().cards().get(1).isFaceDown())
                .isFalse();
    }
}