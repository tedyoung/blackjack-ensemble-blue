package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

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

  @Test
  public void gameViewPopulatesViewModel() throws Exception {
    GameService gameService = new GameService();
    BlackjackController blackjackController = new BlackjackController(gameService);
    blackjackController.startGame();

    Model model = new ConcurrentModel();
    blackjackController.gameView(model);

    assertThat(model.getAttribute("gameView"))
        .isNotNull();
  }

}