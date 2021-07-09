package com.jitterted.ebp.blackjack.domain;

public class Player {

    private final Hand playerHand = new Hand();

    public Player() {
    }

    // This is a temporary scaffolding method!
    public Hand getPlayerHand() {
        return playerHand;
    }

    public boolean isBusted() {
        return getPlayerHand().isBusted();
    }

    boolean hasBlackjack() {
        return getPlayerHand().hasBlackjack();
    }

    void drawFromPlayerDeck(Deck deck) {
        getPlayerHand().drawFrom(deck);
    }

    boolean pushesWith(Hand dealerHand) {
        return getPlayerHand().pushes(dealerHand);
    }

    boolean beats(Hand dealerHand) {
        return getPlayerHand().beats(dealerHand);
    }

    int handValue() {
        return getPlayerHand().value();
    }
}