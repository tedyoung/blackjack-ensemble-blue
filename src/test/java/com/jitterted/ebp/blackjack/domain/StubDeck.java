package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class StubDeck extends Deck {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;
    private final ListIterator<Card> iterator;

    public StubDeck(Rank... ranks) {
        cards = new ArrayList<>();
        for (Rank rank : ranks) {
            cards.add(new FaceUpCard(DUMMY_SUIT, rank));
        }
        this.iterator = cards.listIterator();
    }

    public StubDeck(List<Card> cards) {
        this.cards = cards;
        iterator = cards.listIterator();
    }

    @Override
    public Card draw() {
        return iterator.next();
    }

    @Override
    public String toString() {
        return "StubDeck{" +
                "cards=" + cards +
                '}';
    }
}
