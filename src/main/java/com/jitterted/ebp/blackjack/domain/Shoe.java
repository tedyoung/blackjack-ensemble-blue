package com.jitterted.ebp.blackjack.domain;

import java.util.Iterator;
import java.util.List;

public class Shoe {

    private final Iterator<Deck> deckIterator;
    private Deck deck;

    public Shoe(List<Deck> decks) {
        deckIterator = decks.iterator();
        this.deck = deckIterator.next();
    }

    public Card draw() {
        if (deck.size() == 0) {
            try {
                deck = deckIterator.next();
            } catch (Exception e) {
                throw new ShoeEmpty(e);
            }
        }

        return deck.draw();
    }
}
