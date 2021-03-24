package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.console.ConsoleGame;
import com.jitterted.ebp.blackjack.domain.GameService;

public class Blackjack {

  // This the Application Assembler that configures and starts the application
  public static void main(String[] args) {
    GameService gameService = new GameService();
    ConsoleGame consoleGame = new ConsoleGame(gameService);
    consoleGame.start();
  }
}
