package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static com.jitterted.ebp.blackjack.domain.assertj.BlackjackAssertions.assertThat;

class GameCardFaceTest {

    @Test
    public void playerCardsAreAlwaysFaceUpAfterInitialDeal() {
        final DeckFactory deckFactory = DeckFactory.createForTest(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit());
        Game game = new Game(1, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game)
                .currentPlayerHand()
                .allCardsFaceUp();
    }

    @Test
    public void whenPlayerStandsDealerSecondCardIsFaceUpAfterDealerTurn() {
        final DeckFactory deckFactory = DeckFactory.createForTest(StubDeckBuilder.playerCountOf(1)
                                                                       .addPlayerWithRanks(Rank.TEN, Rank.JACK)
                                                                       .buildWithDealerDoesNotDrawCards());
        Game game = new Game(1, new Shoe(deckFactory.decks()));
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }

    @Test
    public void gameWhereDealerSecondCardIsFaceDownAfterInitialDealWhenPlayerNotDealtBlackjack() {
        final DeckFactory deckFactory = DeckFactory.createForTest(StubDeckBuilder.playerCountOf(1)
                                                                       .addPlayerWithRanks(Rank.TEN, Rank.TWO)
                                                                       .buildWithDealerDoesNotDrawCards());
        Game game = new Game(1, new Shoe(deckFactory.decks()));

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
        final DeckFactory deckFactory = DeckFactory.createForTest(StubDeckBuilder.playerCountOf(1)
                                                                       .addPlayerDealtBlackjack()
                                                                       .buildWithDealerDoesNotDrawCards());
        Game game = new Game(1, new Shoe(deckFactory.decks()));

        game.initialDeal();

        assertThat(game.dealerHand())
                .allCardsFaceUp();
    }
}