package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.DeckFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameServiceTest {
    @Test
    void startGameWithOnePlayerCountIsOne() {
        final Deck deck = new Deck();
        GameService gameService = new GameService(new DeckFactory(deck));

        gameService.createGame(1, new Deck());

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(1);
    }

    @Test
    public void startGameWithTwoPlayersCountIsTwo() throws Exception {
        final Deck deck = new Deck();
        GameService gameService = new GameService(new DeckFactory(deck));

        gameService.createGame(2, new Deck());

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(2);
    }
}