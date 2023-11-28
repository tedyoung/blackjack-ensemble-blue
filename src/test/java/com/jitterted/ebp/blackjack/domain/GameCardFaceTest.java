package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static com.jitterted.ebp.blackjack.domain.assertj.BlackjackAssertions.assertThat;

class GameCardFaceTest {

    @Test
    void playerCardsAreAlwaysFaceUpAfterInitialDeal() {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game)
                .currentPlayerHand()
                .allCardsFaceUp();
    }

    @Test
    void whenPlayerStandsDealerSecondCardIsFaceUpAfterDealerTurn() {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.TEN, Rank.JACK)
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameBuilder.createOnePlayerGamePlaceBetsInitialDeal(deck);

        game.playerStands();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }

    @Test
    void gameWhereDealerSecondCardIsFaceDownAfterInitialDealWhenPlayerNotDealtBlackjack() {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerWithRanks(Rank.TEN, Rank.TWO)
                                       .buildWithDealerDoesNotDrawCards();
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.dealerHand())
                .firstCard()
                .isFaceUp();
        assertThat(game.dealerHand())
                .holeCard()
                .isFaceDown();
    }

    @Test
    void whenOnlyPlayerDealtBlackjackDealerCardIsFaceUpAfterInitialDeal() {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameBuilder.createOnePlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }
}