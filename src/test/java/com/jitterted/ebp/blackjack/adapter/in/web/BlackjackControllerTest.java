package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.GameService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

  @Test
  public void startGameResultsInCardsDealtToPlayer() throws Exception {
    GameService gameService = new GameService();
    BlackjackController blackjackController = new BlackjackController(gameService);

    String redirect = blackjackController.startGame();

    assertThat(redirect)
        .isEqualTo("redirect:/game");

    assertThat(gameService.currentGame())
        .isNotNull();
    assertThat(gameService.currentGame().playerHand().cards())
        .hasSize(2);
  }

}