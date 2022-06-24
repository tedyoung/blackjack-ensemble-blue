package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static com.jitterted.ebp.blackjack.domain.assertj.BlackjackAssertions.assertThat;

class GameCardFaceTest {

    @Test
    public void playerCardsAreAlwaysFaceUpAfterInitialDeal() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);

        game.initialDeal();

        assertThat(game)
                .currentPlayerHand()
                .allCardsFaceUp();
    }

    @Test
    public void whenPlayerStandsDealerSecondCardIsFaceUpAfterDealerTurn() {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerWithRanks(Rank.TEN, Rank.JACK)
                                            .buildWithDealerDoesNotDrawCards(), 1);
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }

    @Test
    public void gameWhereDealerSecondCardIsFaceDownAfterInitialDealWhenPlayerNotDealtBlackjack() {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerWithRanks(Rank.TEN, Rank.TWO)
                                            .buildWithDealerDoesNotDrawCards(), 1);

        game.initialDeal();

        assertThat(game.dealerHand())
                .firstCard()
                .isFaceUp();
        assertThat(game.dealerHand())
                .holeCard()
                .isFaceDown();
    }

    @Test
    public void whenOnlyPlayerDealtBlackjackDealerCardIsFaceUpAfterInitialDeal() {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerDealtBlackjack()
                                            .buildWithDealerDoesNotDrawCards(), 1);

        game.initialDeal();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }
}