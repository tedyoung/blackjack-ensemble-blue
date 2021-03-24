package com.jitterted.ebp.blackjack.domain;

public class GameService {
  private Game currentGame;

  public GameService() {
  }

  public GameService(Game game) {
    this.currentGame = game;
  }

  public void createGame() {
    currentGame = new Game();
  }

  public Game currentGame() {
    if (currentGame == null) {
      throw new IllegalStateException("Game not created");
    }
    return currentGame;
  }
}
