package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.console.ConsoleGame;
import com.jitterted.ebp.blackjack.domain.Game;

public class Blackjack {

  // This the Application Assembler that configures and starts the application
  public static void main(String[] args) {
    Game game = new Game();
    ConsoleGame consoleGame = new ConsoleGame(game); // in general: Entities aren't directly passed in to Adapters
    consoleGame.start();
  }
}
