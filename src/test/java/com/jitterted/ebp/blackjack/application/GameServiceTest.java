package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.domain.Deck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameServiceTest {
    @Test
    void startGameWithOnePlayerCountIsOne() {
        GameService gameService = new GameService(new Deck());

        gameService.createGame(1, new Deck());

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(1);
    }

    @Test
    public void startGameWithTwoPlayersCountIsTwo() throws Exception {
        GameService gameService = new GameService(new Deck());

        gameService.createGame(2, new Deck());

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(2);
    }
}