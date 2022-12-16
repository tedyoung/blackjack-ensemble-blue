package com.jitterted.ebp.blackjack.domain;

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

        this.deckFactory = new DeckFactory(() -> decks.iterator().next());
    }


    public Card draw() {
        if (deck.size() == 0) {
            deck = deckFactory.createDeck();
        }
        return deck.draw();
    }
}
