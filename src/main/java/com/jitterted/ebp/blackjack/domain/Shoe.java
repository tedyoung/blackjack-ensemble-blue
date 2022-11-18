package com.jitterted.ebp.blackjack.domain;

public class Shoe {

    private final DeckFactory deckFactory;
    private Deck deck;

    public Shoe(DeckFactory deckFactory) {
        this.deckFactory = deckFactory;
        this.deck = deckFactory.createDeck();
    }

    public Card draw() {
        if (deck.size() == 0) {
            deck = deckFactory.createDeck();
        }
        return deck.draw();
    }
}
