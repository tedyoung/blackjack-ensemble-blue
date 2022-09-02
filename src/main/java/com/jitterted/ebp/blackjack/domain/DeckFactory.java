package com.jitterted.ebp.blackjack.domain;

public class DeckFactory {
    private Deck deck;

    public DeckFactory() {
        this.deck = new Deck();
    }

    public DeckFactory(Deck deck) {
        this.deck = deck;
    }

    public Deck createDeck() {
        return deck;
    }
}