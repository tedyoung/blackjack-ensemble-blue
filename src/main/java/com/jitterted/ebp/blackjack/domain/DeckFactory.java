package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class DeckFactory {
    private final DeckProvider deckProvider;

    public DeckFactory() {
        this(ShuffledDeck::new);
    }

    public DeckFactory(DeckProvider deckProvider) {
        this.deckProvider = deckProvider;
    }

    public static DeckFactory createForTest(Deck deck) {
//        return List.of(deck);
        return new DeckFactory(deck);
    }

    private DeckFactory(Deck deck) {
        this.deckProvider = () -> deck;
    }

    public Deck createDeck() {
        return deckProvider.next();
    }

    public List<Deck> decks() {
        return List.of(deckProvider.next());
    }
}

interface DeckProvider {
    Deck next();
}