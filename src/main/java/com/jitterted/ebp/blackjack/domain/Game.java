package com.jitterted.ebp.blackjack.domain;

public class Game {

  private final Deck deck;
  private final GameMonitor gameMonitor;

  private final Hand dealerHand = new Hand();
  private final Hand playerHand = new Hand();
  private boolean gameOver;

  public Game() {
    this(new Deck());
  }

  public Game(Deck deck) {
    this(deck, game -> {
    });
  }

  public Game(Deck deck, GameMonitor gameMonitor) {
    this.deck = deck;
    this.gameMonitor = gameMonitor;
  }

  public void initialDeal() {
    dealRoundOfCards();
    dealRoundOfCards();
    updateGameDoneState(playerHand.hasBlackjack());
  }

  private void dealRoundOfCards() {
    // why: players first because this is the rule
    playerHand.drawFrom(deck);
    dealerHand.drawFrom(deck);
  }

  public GameOutcome determineOutcome() {
    if (!isGameOver()) {
      throw new IllegalStateException();
    }
    if(playerHand.hasBlackjack()) {
      return GameOutcome.BLACKJACK;
    }

    if (dealerHand.isBusted()) {
      return GameOutcome.DEALER_BUSTED;
    }
    if (playerHand.isBusted()) {
      return GameOutcome.PLAYER_BUSTED;
    }
    if (playerHand.pushes(dealerHand)) {
      return GameOutcome.PLAYER_PUSHES_DEALER;
    }
    if (playerHand.beats(dealerHand)) {
      return GameOutcome.PLAYER_BEATS_DEALER;
    }
    return GameOutcome.PLAYER_LOSES;
  }

  private void dealerTurn() {
    // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
    if (!playerHand.isBusted()) {
      while (dealerHand.dealerMustDrawCard()) {
        dealerHand.drawFrom(deck);
      }
    }
  }

  public Hand dealerHand() {
    return dealerHand;
  }

  public Hand playerHand() {
    return playerHand;
  }

  public void playerHits() {
    requireGameNotOver();
    playerHand.drawFrom(deck);
    updateGameDoneState(playerHand.isBusted());
  }

  private void requireGameNotOver() {
    if (isGameOver()) {
      throw new IllegalStateException();
    }
  }

  private void updateGameDoneState(boolean playerDone) {
    this.gameOver = playerDone;
    if (playerDone) {
      roundCompleted();
    }
  }

  public void playerStands() {
    requireGameNotOver();
    dealerTurn();
    updateGameDoneState(true);
  }

  private void roundCompleted() {
    gameMonitor.roundCompleted(this);
  }

  public boolean isGameOver() {
    return gameOver;
  }
}
