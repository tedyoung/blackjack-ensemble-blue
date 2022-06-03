package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameCardFaceTest {

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

        assertThatDealerFirstCardIsFaceUp(game);
        assertThatDealerHoleCardIsFaceUp(game);
    }

    @Test
    public void gameWhereDealerSecondCardIsFaceDownAfterInitialDealWhenPlayerNotDealtBlackjack() {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerWithRanks(Rank.TEN, Rank.TWO)
                                            .buildWithDealerDoesNotDrawCards(), 1);

        game.initialDeal();

        assertThatDealerFirstCardIsFaceUp(game);
        assertThatDealerHoleCardIsFaceDown(game);
    }

    @Test
    public void whenOnlyPlayerDealtBlackjackDealerCardIsFaceUpAfterInitialDeal() {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerDealtBlackjack()
                                            .buildWithDealerDoesNotDrawCards(), 1);

        game.initialDeal();

        assertThatDealerFirstCardIsFaceUp(game);
        assertThatDealerHoleCardIsFaceUp(game);
    }

    private void assertThatDealerHoleCardIsFaceDown(Game game) {
        assertThat(isDealerSecondCardFaceDown(game))
                .isTrue();
    }

    private void assertThatDealerHoleCardIsFaceUp(Game game) {
        assertThat(isDealerSecondCardFaceDown(game))
                .isFalse();
    }

    private void assertThatDealerFirstCardIsFaceUp(Game game) {
        assertThat(isDealerFirstCardFaceDown(game))
                .isFalse();
    }

    private boolean isDealerFirstCardFaceDown(Game game) {
        return game.dealerHand().cards().get(0).isFaceDown();
    }

    private boolean isDealerSecondCardFaceDown(Game game) {
        return game.dealerHand().cards().get(1).isFaceDown();
    }
}