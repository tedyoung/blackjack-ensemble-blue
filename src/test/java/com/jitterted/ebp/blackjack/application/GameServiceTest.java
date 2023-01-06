package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.DeckFactory;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.ShuffledDeck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameServiceTest {
    @Test
    void startGameWithOnePlayerCountIsOne() {
        Deck deck = new ShuffledDeck();
        final DeckFactory deckFactory = DeckFactory.createForTest(deck);
        GameService gameService = GameService.createForTest(new Shoe(deckFactory.decks()));

        gameService.createGame(1);

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(1);
    }

    @Test
    public void startGameWithTwoPlayersCountIsTwo() throws Exception {
        Deck deck = new ShuffledDeck();
        final DeckFactory deckFactory = DeckFactory.createForTest(deck);
        GameService gameService = GameService.createForTest(new Shoe(deckFactory.decks()));

        gameService.createGame(2);

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(2);
    }
}