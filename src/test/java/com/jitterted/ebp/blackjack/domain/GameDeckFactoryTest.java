package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.application.GameService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ListIterator;

import static org.assertj.core.api.Assertions.*;

public class GameDeckFactoryTest {

    @Test
    public void withOnePlayerInitialDealDoesNotRunOutOfCards() throws Exception {
        DeckProvider deckProvider = () -> StubDeckBuilder.playerCountOf(1)
                                                         .addPlayerDealtBlackjack()
                                                         .buildWithDealerDoesNotDrawCards();
        GameService gameService = new GameService(new DeckFactory(deckProvider));
        gameService.createGame(1);
        gameService.initialDeal();

        gameService.createGame(1);
        gameService.initialDeal();

        // successful initial deal?
        assertThat(gameService.currentGame().currentPlayerCards())
                .hasSize(2);
    }

    @Test
    public void whenFirstDeckIsExhaustedOnDealersTurnThenNextDeckIsCreated() throws Exception {
        Deck firstDeck = new StubDeck(Rank.TWO, Rank.THREE, Rank.TEN);
        Deck secondDeck = new StubDeck(Rank.ACE, Rank.JACK);

        DeckProvider deckProvider = new StubDeckProvider(firstDeck, secondDeck);
        GameService gameService = new GameService(new DeckFactory(deckProvider));
        gameService.createGame(1);

        gameService.initialDeal();

        assertThat(firstDeck.size())
                .isZero();
        assertThat(secondDeck.size())
                .isEqualTo(1);
    }

    @Test
    public void whenFirstDeckIsExhaustedOnPlayersTurnThenNextDeckIsCreated() throws Exception {
        Deck firstDeck = new StubDeck(Rank.TWO, Rank.NINE);
        Deck secondDeck = new StubDeck(Rank.ACE, Rank.JACK);

        DeckProvider deckProvider = new StubDeckProvider(firstDeck, secondDeck);
        GameService gameService = new GameService(new DeckFactory(deckProvider));
        gameService.createGame(1);

        gameService.initialDeal();

        assertThat(firstDeck.size())
                .isZero();
        assertThat(secondDeck.size())
                .isZero();
    }

    @Test
    public void withTwoPlayersInitialDealDoesNotRunOutOfCards() throws Exception {
        DeckProvider deckProvider = () -> StubDeckBuilder.playerCountOf(1)
                                                         .addPlayerDealtBlackjack()
                                                         .buildWithDealerDoesNotDrawCards();
        GameService gameService = new GameService(new DeckFactory(deckProvider));
        gameService.createGame(2);
        gameService.initialDeal();

        // successful initial deal?
        assertThat(gameService.currentGame().currentPlayerCards())
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
