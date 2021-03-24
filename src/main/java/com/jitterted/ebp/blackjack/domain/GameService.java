package com.jitterted.ebp.blackjack.domain;

public class GameService {
  private final Deck deck;
  private Game currentGame;

  public GameService() {
    this.deck = new Deck();
  }

  public GameService(Deck deck) {
    this.deck = deck;
  }

  public void createGame() {
    currentGame = new Game(deck);
  }

  public Game currentGame() {
    if (currentGame == null) {
      throw new IllegalStateException("Game not created");
    }
    return currentGame;
  }
}
