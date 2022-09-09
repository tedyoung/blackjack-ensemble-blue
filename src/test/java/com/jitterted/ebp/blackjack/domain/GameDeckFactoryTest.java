package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.application.GameService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
    @Disabled
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

    @Test
    public void newDeckCreatedIfOutOfCards() throws Exception {
        DeckProvider deckProvider = () -> StubDeckBuilder.playerCountOf(1)
                                                         .addPlayerDealtBlackjack()
                                                         .buildWithDealerDoesNotDrawCards();
        GameService gameService = new GameService(new DeckFactory(deckProvider));
        gameService.createGame(2);

        Deck deck = new StubDeck(Rank.TWO);
        deck.draw();
        assertThat(deck.size())
                .isEqualTo(0);

        Deck newDeckIfNotEnoughCards = gameService
                .currentGame().createNewDeckIfNotEnoughCards(deck);

        assertThat(newDeckIfNotEnoughCards).isNotSameAs(deck);
    }
}
