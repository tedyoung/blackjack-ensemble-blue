package com.jitterted.ebp.blackjack.domain;

public class GameService {
  private final Deck deck;
  private final GameMonitor gameMonitor;
  private Game currentGame;

  public GameService() {
    this(new Deck());
  }

  public GameService(Deck deck) {
    this.deck = deck;
    this.gameMonitor = game -> {};
  }

  public GameService(GameMonitor gameMonitor) {
    this.gameMonitor = gameMonitor;
    this.deck = new Deck();
  }

  public void createGame() {
    currentGame = new Game(deck, gameMonitor);
  }

  public Game currentGame() {
    if (currentGame == null) {
      throw new IllegalStateException("Game not created");
    }
    return currentGame;
  }
}
