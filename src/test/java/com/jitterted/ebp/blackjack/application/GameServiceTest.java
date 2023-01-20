package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.OrderedDeck;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameServiceTest {
    @Test
    void startGameWithOnePlayerCountIsOne() {
        GameService gameService = GameService.createForTest();

        gameService.createGame(1, new Shoe(List.of(new OrderedDeck())));

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(1);
    }

    @Test
    public void startGameWithTwoPlayersCountIsTwo() throws Exception {
        GameService gameService = GameService.createForTest();

        gameService.createGame(2, new Shoe(List.of(new OrderedDeck())));

        assertThat(gameService.currentGame().playerCount())
                .isEqualTo(2);
    }

    // create a GameService
    // createGame() (with no shoe)
    // expect it to use the shuffled numbers that we're going to pass in via the Shuffler
    // our test double for the Shuffler will return 0..51
    // observe it via player hand

    @Test
    public void createGameCreatesShuffledDeckUsingShuffler() throws Exception {
        GameService gameService = GameService.createForTest();
        gameService.createGame(1);

        gameService.initialDeal();

        assertThat(gameService.currentGame().currentPlayerCards())
                .containsExactly(new Card(Suit.HEARTS, Rank.ACE),
                                 new Card(Suit.HEARTS, Rank.THREE));
    }

}