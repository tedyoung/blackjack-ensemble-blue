package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class Player {

    private final Hand playerHand = new Hand();
    private int id = 0;
    private boolean isDone = false;

    public Player() {
    }

    public Player(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public void drawFrom(Deck deck) {
        playerHand.drawFrom(deck);
        if (hasBlackjack()) {
            done();
        }
    }

    public void hit(Deck deck) {
        requireNotDone();
        playerHand.drawFrom(deck);
        if (isBusted()) {
            done();
        }
    }

    public void stand() {
        requireNotDone();
        done();
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

    private boolean beats(Hand dealerHand) {
        return playerHand.beats(dealerHand);
    }

    private boolean isBusted() {
        return playerHand.isBusted();
    }

    private boolean pushesWith(Hand dealerHand) {
        return playerHand.pushes(dealerHand);
    }

    private boolean hasBlackjack() {
        return playerHand.hasBlackjack();
    }

    private void done() {
        isDone = true;
    }

    private void requireDone() {
        if (!isDone) {
            throw new IllegalStateException();
        }
    }

    private void requireNotDone() {
        if (isDone) {
            throw new IllegalStateException();
        }
    }
}