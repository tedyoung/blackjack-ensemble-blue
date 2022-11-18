package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ListIterator;

import static org.assertj.core.api.Assertions.*;

public class GameShoeTest {

    @Test
    public void withOnePlayerInitialDealDoesNotRunOutOfCards() throws Exception {
        DeckProvider deckProvider = () -> StubDeckBuilder.playerCountOf(1)
                                                         .addPlayerDealtBlackjack()
                                                         .buildWithDealerDoesNotDrawCards();
        DeckFactory deckFactory = new DeckFactory(deckProvider);
        Shoe shoe = new Shoe(deckFactory);
        Game firstGame = new Game(1, shoe);
        Game secondGame = new Game(1, shoe);
        firstGame.initialDeal();

        secondGame.initialDeal();

        assertThat(secondGame.currentPlayerCards())
                .hasSize(2);
    }

    @Test
    public void whenFirstDeckIsExhaustedOnDealersTurnThenNextDeckIsCreated() throws Exception {
        Deck firstDeck = new StubDeck(Rank.TWO, Rank.THREE, Rank.TEN);
        Deck secondDeck = new StubDeck(Rank.ACE, Rank.JACK);

        DeckProvider deckProvider = new StubDeckProvider(firstDeck, secondDeck);
        DeckFactory deckFactory = new DeckFactory(deckProvider);
        Shoe shoe = new Shoe(deckFactory);
        Game game = new Game(1, shoe);

        game.initialDeal();

        assertThat(shoe.draw().rank())
                .isEqualTo(Rank.JACK);
    }

    @Test
    public void whenFirstDeckIsExhaustedOnPlayersTurnThenNextDeckIsCreated() throws Exception {
        Deck firstDeck = new StubDeck(Rank.TWO, Rank.NINE);
        Deck secondDeck = new StubDeck(Rank.ACE, Rank.JACK, Rank.TEN);

        DeckProvider deckProvider = new StubDeckProvider(firstDeck, secondDeck);
        DeckFactory deckFactory = new DeckFactory(deckProvider);
        Shoe shoe = new Shoe(deckFactory);
        Game game = new Game(1, shoe);

        game.initialDeal();

        assertThat(shoe.draw().rank())
                .isEqualTo(Rank.TEN);
    }

    @Test
    public void withTwoPlayersInitialDealDoesNotRunOutOfCards() throws Exception {
        // purposely setting player count to 1 so that it runs out of cards for 2 player game
        DeckProvider deckProvider = () -> StubDeckBuilder.playerCountOf(1)
                                                         .addPlayerDealtBlackjack()
                                                         .buildWithDealerDoesNotDrawCards();
        DeckFactory deckFactory = new DeckFactory(deckProvider);
        Shoe shoe = new Shoe(deckFactory);
        Game game = new Game(2, shoe);
        game.initialDeal();

        assertThat(game.currentPlayerCards())
                .hasSize(2);
    }

    private static class StubDeckProvider implements DeckProvider {
        private final ListIterator<Deck> deckIterator;

        public StubDeckProvider(Deck... decks) {
            deckIterator = Arrays.stream(decks).toList().listIterator();
        }

        @Override
        public Deck next() {
            return deckIterator.next();
        }
    }
}
