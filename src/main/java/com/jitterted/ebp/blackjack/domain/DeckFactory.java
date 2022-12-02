package com.jitterted.ebp.blackjack.domain;

public class DeckFactory {
    private final DeckProvider deckProvider;

    public DeckFactory() {
        this(ShuffledDeck::new);
    }

    public DeckFactory(DeckProvider deckProvider) {
        this.deckProvider = deckProvider;
    }

    public static DeckFactory createForTest(Deck deck) {
        return new DeckFactory(deck);
    }

    private DeckFactory(Deck deck) {
        this.deckProvider = () -> deck;
    }

    public Deck createDeck() {
        return deckProvider.next();
    }
}

interface DeckProvider {
    Deck next();
}