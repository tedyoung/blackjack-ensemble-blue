package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jitterted.ebp.blackjack.domain.assertj.BlackjackAssertions.assertThat;

class GameCardFaceTest {

    @Test
    public void playerCardsAreAlwaysFaceUpAfterInitialDeal() {
        final List<Deck> deckFactory = List.of(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());
        Game game = new Game(1, new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game)
                .currentPlayerHand()
                .allCardsFaceUp();
    }

    @Test
    public void whenPlayerStandsDealerSecondCardIsFaceUpAfterDealerTurn() {
        final List<Deck> deckFactory = List.of(StubDeckBuilder.playerCountOf(1)
                                                              .addPlayerWithRanks(Rank.TEN, Rank.JACK)
                                                              .buildWithDealerDoesNotDrawCards());
        Game game = new Game(1, new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }

    @Test
    public void gameWhereDealerSecondCardIsFaceDownAfterInitialDealWhenPlayerNotDealtBlackjack() {
        final List<Deck> deckFactory = List.of(StubDeckBuilder.playerCountOf(1)
                                                              .addPlayerWithRanks(Rank.TEN, Rank.TWO)
                                                              .buildWithDealerDoesNotDrawCards());
        Game game = new Game(1, new Shoe(deckFactory));

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
        final List<Deck> deckFactory = List.of(StubDeckBuilder.playerCountOf(1)
                                                              .addPlayerDealtBlackjack()
                                                              .buildWithDealerDoesNotDrawCards());
        Game game = new Game(1, new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }
}