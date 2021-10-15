package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.adapter.in.web.BlackjackController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class GameOverTest {
    @Test
    public void twoPlayerGameOnePlayerStandsGameIsInProgress() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersNotDealtBlackjack();
        GameService gameService = new GameService(deck);
        BlackjackController blackjackController = new BlackjackController(gameService);
        blackjackController.startGame(2);

        String redirectPage = blackjackController.standCommand();

        assertThat(redirectPage)
                .isEqualTo("redirect:/game");

    }
}