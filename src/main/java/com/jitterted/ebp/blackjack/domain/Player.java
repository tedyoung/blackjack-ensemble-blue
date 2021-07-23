package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class Player {

    private final Hand playerHand = new Hand();
    private boolean isDone = false;

    public boolean isBusted() {
        return playerHand.isBusted();
    }

    public boolean hasBlackjack() {
        return playerHand.hasBlackjack();
    }

    public void drawFrom(Deck deck) {
        playerHand.drawFrom(deck);
    }

    public boolean pushesWith(Hand dealerHand) {
        return playerHand.pushes(dealerHand);
    }

    public boolean beats(Hand dealerHand) {
        return playerHand.beats(dealerHand);
    }

    public int handValue() {
        return playerHand.value();
    }

    public List<Card> cards() {
        return playerHand.cards();
    }

    public boolean isDone() {
        return isDone;
    }

    public void done() {
        isDone = true;
    }
}