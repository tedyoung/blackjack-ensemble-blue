package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.ShuffledDeck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameServiceTest {
    @Test
    void startGameWithOnePlayerCountIsOne() {
        GameService gameService = GameService.createForTest();

        gameService.createGame(1, new Shoe(List.of(new ShuffledDeck())));

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(1);
    }

    @Test
    public void startGameWithTwoPlayersCountIsTwo() throws Exception {
        GameService gameService = GameService.createForTest();

        gameService.createGame(2, new Shoe(List.of(new ShuffledDeck())));

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(2);
    }
}