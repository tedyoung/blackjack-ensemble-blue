package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class DealerHand extends Hand {

    boolean dealerMustDrawCard() {
        return value() <= 16;
    }

    @Override
    // As a Query method, this must return a SNAPSHOT, not a live "View"
    public List<Card> cards() {
        return List.copyOf(cards);
    }

    @Override
    public void drawFrom(Deck deck) {
        Card draw = turnToFaceDownIfHoleCard(deck.draw());
        cards.add(draw);
    }

    public void flipTheFaceDownCardUp() {
        if (cards.isEmpty()) {
            return;
        }

        Card originalCard = holeCard();
        cards.set(1, originalCard);
    }

    private Card turnToFaceDownIfHoleCard(Card draw) {
        if (cards.size() == 1) {
            draw = new FaceDownCard(draw);
        }
        return draw;
    }

    private Card holeCard() {
        return ((FaceDownCard) cards.get(1)).originalCard();
    }
}