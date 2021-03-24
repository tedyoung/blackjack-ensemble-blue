package com.jitterted.ebp.blackjack.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlackjackController {

  @PostMapping("/start-game")
  public String startGame() {
    return "blackjack";
  }
}
