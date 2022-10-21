package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class StubDeck extends ShuffledDeck {

    private static final Suit DUMMY_SUIT = Suit.HEARTS;

    public StubDeck(Rank... ranks) {
        cards = new ArrayList<>();
        for (Rank rank : ranks) {
            cards.add(new Card(DUMMY_SUIT, rank));
        }
    }

    public StubDeck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public String convertToString() {
        String customDeck = "";

        for (Card card : cards) {
            customDeck += card.rank().display() + ",";
        }
        return customDeck;
    }
}
