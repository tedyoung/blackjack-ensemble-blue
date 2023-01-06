package com.jitterted.ebp.blackjack.domain;

import java.util.Iterator;
import java.util.List;

public class Shoe {

    private final DeckFactory deckFactory;
    private Deck deck;

    @Deprecated
    public Shoe(DeckFactory deckFactory) {
        this.deckFactory = deckFactory;
        this.deck = deckFactory.createDeck();
    }

    public Shoe(List<Deck> decks) {
        Iterator<Deck> deckIterator = decks.iterator();
        this.deckFactory = new DeckFactory(deckIterator::next);
        this.deck = deckFactory.createDeck();
    }

    public Card draw() {
        if (deck.size() == 0) {
            try {
                deck = deckFactory.createDeck();
            } catch (Exception e) {
                throw new ShoeEmpty(e);
            }
        }

        return deck.draw();
    }
}
