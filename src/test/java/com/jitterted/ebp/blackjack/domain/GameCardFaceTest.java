package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static com.jitterted.ebp.blackjack.domain.assertj.BlackjackAssertions.assertThat;

class GameCardFaceTest {

    @Test
    public void playerCardsAreAlwaysFaceUpAfterInitialDeal() {
        Game game = new Game(1, new DeckFactory(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit()));

        game.initialDeal();

        assertThat(game)
                .currentPlayerHand()
                .allCardsFaceUp();
    }

    @Test
    public void whenPlayerStandsDealerSecondCardIsFaceUpAfterDealerTurn() {
        Game game = new Game(1, new DeckFactory(StubDeckBuilder.playerCountOf(1)
                                                               .addPlayerWithRanks(Rank.TEN, Rank.JACK)
                                                               .buildWithDealerDoesNotDrawCards()));
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }

    @Test
    public void gameWhereDealerSecondCardIsFaceDownAfterInitialDealWhenPlayerNotDealtBlackjack() {
        Game game = new Game(1, new DeckFactory(StubDeckBuilder.playerCountOf(1)
                                                               .addPlayerWithRanks(Rank.TEN, Rank.TWO)
                                                               .buildWithDealerDoesNotDrawCards()));

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
        Game game = new Game(1, new DeckFactory(StubDeckBuilder.playerCountOf(1)
                                                               .addPlayerDealtBlackjack()
                                                               .buildWithDealerDoesNotDrawCards()));

        game.initialDeal();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }
}