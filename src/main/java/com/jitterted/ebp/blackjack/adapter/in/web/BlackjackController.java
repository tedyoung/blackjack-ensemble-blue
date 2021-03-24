package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlackjackController {

  private final GameService gameService;

  @Autowired
  public BlackjackController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/start-game")
  public String startGame() {
    gameService.createGame();
    gameService.currentGame().initialDeal();
    return "redirect:/game";
  }

  @GetMapping("/game")
  public String gameView() {
    return "blackjack";
  }

}
