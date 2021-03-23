package com.jitterted.ebp.blackjack;

public class ConsoleGame {

  private final Game game;

  public ConsoleGame(Game game) {
    this.game = game;
  }

  public static void main(String[] args) {
    Game game = new Game();
    ConsoleGame consoleGame = new ConsoleGame(game); // in general: Entities aren't directly passed in to Adapters
    consoleGame.start();
  }

  public void start() {
    Game.displayWelcomeScreen();

    game.initialDeal();

    playerPlays();

    game.dealerTurn();

    game.displayFinalGameState();

    game.determineOutcome();

    Game.resetScreen();
  }

  public void playerPlays() {
    while (!game.isPlayerDone()) {
      game.displayGameState();
      String command = game.inputFromPlayer();
      handle(command);
    }
  }

  public void handle(String command) {
    if (command.toLowerCase().startsWith("h")) {
      game.playerHits();
    } else if (command.toLowerCase().startsWith("s")) {
      game.playerStands();
    }
  }

}
