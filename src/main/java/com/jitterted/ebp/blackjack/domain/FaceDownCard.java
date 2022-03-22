package com.jitterted.ebp.blackjack.domain;

public class FaceDownCard implements Card {
    private Card card;

    public FaceDownCard(Card card) {

        this.card = card;
    }

    @Override
    public boolean isFaceDown() {
        return true;
    }

    @Override
    public int rankValue() {
        return card.rankValue();
    }

    @Override
    public Suit suit() {
        return card.suit();
    }

    @Override
    public Rank rank() {
        return card.rank();
    }
}
