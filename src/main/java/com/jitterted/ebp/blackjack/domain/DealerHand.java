package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class DealerHand extends Hand {

    private static final int HOLE_CARD_INDEX = 1;

    boolean dealerMustDrawCard() {
        return value() <= 16;
    }

    @Override
    // As a Query method, this must return a SNAPSHOT, not a live "View"
    public List<Card> cards() {
        return List.copyOf(cards);
    }

    @Override
    public void drawFrom(Shoe shoe) {
        cards.add(shoe.draw());
        if (isMostRecentCardTheHoleCard()) {
            holeCard().flip();
        }
    }

    public void flipTheHoleCardUp() {
        if (cards.isEmpty()) {
            return;
        }

        holeCard().flip();
    }

    private boolean isMostRecentCardTheHoleCard() {
        return cards.size() == 2;
    }

    private Card holeCard() {
        return cards.get(HOLE_CARD_INDEX);
    }

}