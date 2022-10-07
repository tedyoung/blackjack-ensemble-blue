package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class Player {

    private final Hand playerHand = new Hand();
    private int id = 0;
    private boolean isDone = false;
    private PlayerReasonDone reasonDone;

    public Player() {
    }

    public Player(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public Deck initialDrawFrom(Deck deck, DeckFactory deckFactory) {
        deck = playerHand.drawFromWithFactory(deckFactory, deck);
        if (hasBlackjack()) {
            done();
            reasonDone = PlayerReasonDone.PLAYER_HAS_BLACKJACK;
        }
        return deck;
    }

    public void hit(Deck deck) {
        requireNotDone();
        playerHand.drawFrom(deck);
        if (isBusted()) {
            done();
            reasonDone = PlayerReasonDone.PLAYER_BUSTED;
        }
    }

    public void stand() {
        requireNotDone();
        done();
        reasonDone = PlayerReasonDone.PLAYER_STANDS;
    }

    void doneDealerDealtBlackjack() {
        done();
        reasonDone = PlayerReasonDone.DEALER_DEALT_BLACKJACK;
    }

    public int handValue() {
        return playerHand.value();
    }

    public List<Card> cards() {
        return playerHand.cards();
    }

    // Player is done when:
    // - player is dealt blackjack
    // - player stands
    // - player goes bust
    public boolean isDone() {
        return isDone;
    }

    public PlayerOutcome outcome(Hand dealerHand) {
        requireDone();
        if (isBusted()) {
            return PlayerOutcome.PLAYER_BUSTED;
        }
        if (dealerHand.isBusted()) {
            return PlayerOutcome.DEALER_BUSTED;
        }
        if (hasBlackjack() && !dealerHand.hasBlackjack()) {
            return PlayerOutcome.BLACKJACK;
        }
        if (pushesWith(dealerHand)) {
            return PlayerOutcome.PLAYER_PUSHES_DEALER;
        }
        if (beats(dealerHand)) {
            return PlayerOutcome.PLAYER_BEATS_DEALER;
        }
        return PlayerOutcome.PLAYER_LOSES;
    }

    void done() {
        isDone = true;
    }

    public PlayerReasonDone reasonDone() {
        requireDone();
        return reasonDone;
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
}