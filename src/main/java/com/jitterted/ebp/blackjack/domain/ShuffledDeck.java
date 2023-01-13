package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShuffledDeck implements Deck {
    protected List<Card> cards = new ArrayList<>();

    @Deprecated
    public  ShuffledDeck() {
        cards = createOrderedCards();
        Collections.shuffle(cards);
    }

    public ShuffledDeck(List<Integer> cardOrderIndexes) {
        List<Card> orderedCards = createOrderedCards();
        reorderCards(orderedCards, cardOrderIndexes);
    }

    private void reorderCards(List<Card> orderedCards, List<Integer> cardOrderIndexes) {
        requireNotTooManyCardOrderIndexes(orderedCards, cardOrderIndexes);
        requireValidCardOrderIndexes(cardOrderIndexes, orderedCards.size());
        requireUniqueCardOrderIndexes(cardOrderIndexes);

        for (Integer cardNumber : cardOrderIndexes) {
            cards.add(orderedCards.get(cardNumber));
        }
    }

    private void requireNotTooManyCardOrderIndexes(List<Card> orderedCards, List<Integer> cardOrderIndexes) {
        if (cardOrderIndexes.size() > orderedCards.size()) {
            throw new IllegalArgumentException("Too many card indexes");
        }
    }

    private void requireValidCardOrderIndexes(List<Integer> cardOrderIndexes, int numberOfCards) {
        if (cardOrderIndexes.stream().anyMatch(n -> n >= numberOfCards)) {
            throw new IllegalArgumentException("Card index is out of range, must be within 0 to 51");
        }
    }

    private void requireUniqueCardOrderIndexes(List<Integer> cardOrderIndexes) {
        if (cardOrderIndexes.stream().distinct().count() != cardOrderIndexes.size()) {
            throw new IllegalArgumentException("Found duplicate card indexes");
        }
    }

    private List<Card> createOrderedCards() {
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
