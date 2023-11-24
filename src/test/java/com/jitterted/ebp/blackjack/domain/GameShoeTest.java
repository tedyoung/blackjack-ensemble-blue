package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameShoeTest {

    @Test
    public void withOnePlayerInitialDealDoesNotRunOutOfCards() throws Exception {
        Deck deck1 = StubDeckBuilder.playerCountOf(1)
                                    .addPlayerDealtBlackjack()
                                    .buildWithDealerDoesNotDrawCards();
        Deck deck2 = StubDeckBuilder.playerCountOf(1)
                                    .addPlayerDealtBlackjack()
                                    .buildWithDealerDoesNotDrawCards();

        Shoe shoe = new Shoe(List.of(deck1, deck2));
        Game firstGame = GameBuilder.createOnePlayerGamePlaceBets(shoe);
        Game secondGame = GameBuilder.createOnePlayerGamePlaceBets(shoe);
        firstGame.initialDeal();

        secondGame.initialDeal();

        assertThat(secondGame.currentPlayerCards())
                .hasSize(2);
    }

    @Test
    public void whenFirstDeckIsExhaustedOnDealersTurnThenNextDeckIsCreated() throws Exception {
        Deck firstDeck = new StubDeck(Rank.TWO, Rank.THREE, Rank.TEN);
        Deck secondDeck = new StubDeck(Rank.ACE, Rank.JACK);
        Shoe shoe = new Shoe(List.of(firstDeck, secondDeck));
        Game game = GameBuilder.createOnePlayerGamePlaceBets(shoe);

        game.initialDeal();

        assertThat(shoe.draw().rank())
                .isEqualTo(Rank.JACK);
    }

    @Test
    public void whenFirstDeckIsExhaustedOnPlayersTurnThenNextDeckIsCreated() throws Exception {
        Deck firstDeck = new StubDeck(Rank.TWO, Rank.NINE);
        Deck secondDeck = new StubDeck(Rank.ACE, Rank.JACK, Rank.TEN);
        Shoe shoe = new Shoe(List.of(firstDeck, secondDeck));
        Game game = GameBuilder.createOnePlayerGamePlaceBets(shoe);

        game.initialDeal();

        assertThat(shoe.draw().rank())
                .isEqualTo(Rank.TEN);
    }

    @Test
    public void whenAllDecksAreExhaustedThrowsException() throws Exception {
        Deck deck = new StubDeck(Rank.TWO, Rank.NINE);
        Game game = GameBuilder
                .createTwoPlayerGamePlaceBets(deck, PlayerId.of(44), PlayerId.of(17));

        assertThatThrownBy(game::initialDeal)
                .isExactlyInstanceOf(ShoeEmpty.class);
    }

}
