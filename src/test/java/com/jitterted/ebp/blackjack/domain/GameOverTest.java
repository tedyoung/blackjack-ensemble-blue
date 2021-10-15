package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.adapter.in.web.BlackjackController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class GameOverTest {
    @Test
    public void twoPlayerGameOnePlayerStandsGameIsInProgress() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        Game game = new Game(deck, 2);
        game.initialDeal();

        game.playerStands();

        assertThat(game.isGameOver())
                .isFalse();
    }

    @Test
    public void threePlayerGameTwoPlayersStandGameIsNotOver() {
        Deck deck = MultiPlayerStubDeckFactory.threePlayersNotDealtBlackjack();
        Game game = new Game(deck, 3);
        game.initialDeal();


        assertThat(game.isGameOver())
                .isFalse();

    }
}