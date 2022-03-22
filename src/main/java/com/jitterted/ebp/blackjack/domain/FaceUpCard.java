package com.jitterted.ebp.blackjack.domain;

public class FaceUpCard implements Card {
    private final Suit suit;
    private final Rank rank;

    public FaceUpCard(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public int rankValue() {
        return rank.value();
    }

    @Override
    public Suit suit() {
        return suit;
    }

    @Override
    public Rank rank() {
        return rank;
    }

    @Override
    public String toString() {
        return "Card {" +
                "suit=" + suit +
                ", rank=" + rank +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FaceUpCard card = (FaceUpCard) o;

        if (!suit.equals(card.suit)) return false;
        return rank.equals(card.rank);
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }

    @Override
    public boolean isFaceDown() {
        return false;
    }
}