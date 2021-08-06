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
        if (hasBlackjack()) {
            done();
        }
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

    public void requireDone() {
        if (!isDone) {
            throw new IllegalStateException();
        }
    }

    public void requireNotDone() {
        if (isDone()) {
            throw new IllegalStateException();
        }
    }

    public PlayerOutcome outcome(Hand dealerHand) {
        requireDone();
        if (hasBlackjack()) {
            return PlayerOutcome.BLACKJACK;
        }
        if (dealerHand.isBusted()) {
            return PlayerOutcome.DEALER_BUSTED;
        }
        if (isBusted()) {
            return PlayerOutcome.PLAYER_BUSTED;
        }
        if (pushesWith(dealerHand)) {
            return PlayerOutcome.PLAYER_PUSHES_DEALER;
        }
        if (beats(dealerHand)) {
            return PlayerOutcome.PLAYER_BEATS_DEALER;
        }
        return PlayerOutcome.PLAYER_LOSES;
    }

    public void hit(Deck deck) {
        if (isDone()) {
            throw new IllegalStateException();
        }
        drawFrom(deck);
        if (isBusted()) {
            done();
        }
    }

    public void stand() {
        requireNotDone();
        done();
    }
}