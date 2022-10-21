package com.jitterted.ebp.blackjack.domain;

public class Shoe implements Deck {

    private final DeckFactory deckFactory;
    private Deck deck;

    public Shoe(DeckFactory deckFactory) {
        this.deckFactory = deckFactory;
        this.deck = deckFactory.createDeck();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Card draw() {
        if (deck.size() == 0) {
            deck = deckFactory.createDeck();
        }
        return deck.draw();
    }
}
