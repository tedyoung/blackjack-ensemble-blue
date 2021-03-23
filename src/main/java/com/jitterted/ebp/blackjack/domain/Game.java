package com.jitterted.ebp.blackjack.domain;

public class Game {

  private final Deck deck;

  private final Hand dealerHand = new Hand();
  private final Hand playerHand = new Hand();
  private boolean playerDone;

  public Game() {
    this(new Deck());
  }

  public Game(Deck deck) {
    this.deck = deck;
  }

  public void initialDeal() {
    dealRoundOfCards();
    dealRoundOfCards();
  }

  private void dealRoundOfCards() {
    // why: players first because this is the rule
    playerHand.drawFrom(deck);
    dealerHand.drawFrom(deck);
  }

  public GameOutcome determineOutcome() {
    if (playerHand.isBusted()) {
      return GameOutcome.PLAYER_BUSTED;
    } else if (dealerHand.isBusted()) {
      return GameOutcome.DEALER_BUSTED;
    } else if (playerHand.beats(dealerHand)) {
      return GameOutcome.PLAYER_BEATS_DEALER;
    } else if (playerHand.pushes(dealerHand)) {
      return GameOutcome.PLAYER_PUSHES_DEALER;
    } else {
      return GameOutcome.PLAYER_LOSES;
    }
  }

  public void dealerTurn() {
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
    playerHand.drawFrom(deck);
    playerDone = playerHand.isBusted();
  }

  public void playerStands() {
    playerDone = true;
  }

  public boolean isPlayerDone() {
    return playerDone;
  }
}
