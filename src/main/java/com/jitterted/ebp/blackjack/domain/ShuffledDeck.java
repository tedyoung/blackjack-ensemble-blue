package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShuffledDeck implements Deck {
    protected List<Card> cards = new ArrayList<>();

    public ShuffledDeck() {
        cards = createOrderedCards();
        Collections.shuffle(cards);
    }

    public ShuffledDeck(List<Integer> cardNumbers) {
        List<Card> orderedCards = createOrderedCards();
        for (Integer cardNumber : cardNumbers) {
            cards.add(orderedCards.get(cardNumber));
        }
    }

    private static List<Card> createOrderedCards() {
        List<Card> orderedCards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                orderedCards.add(new Card(suit, rank));
            }
        }
        return orderedCards;
    }

    @Override
    public int size() {
        return cards.size();
    }

    @Override
    public Card draw() {
        return cards.remove(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShuffledDeck deck = (ShuffledDeck) o;
        return cards.equals(deck.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}
