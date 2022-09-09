package com.jitterted.ebp.blackjack.domain;

public class DeckFactory {
    private final DeckProvider deckProvider;

    public DeckFactory() {
        this(new Deck());
    }

    public DeckFactory(Deck deck) {
        this.deckProvider = () -> deck;
    }

    public DeckFactory(DeckProvider deckProvider) {
        this.deckProvider = deckProvider;
    }

    public Deck createDeck() {
        return deckProvider.next();
    }
}

interface DeckProvider {
    Deck next();
}