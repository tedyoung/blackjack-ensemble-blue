package com.jitterted.ebp.blackjack.domain;

public class DefaultCard implements Card {

    private final Suit suit;
    private final Rank rank;
    private Face face = Face.UP;

    public DefaultCard(Suit suit, Rank rank) {
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
    public boolean isFaceDown() {
        return face.isValue();
    }

    @Override
    public void flip() {
        if (face == Face.DOWN) {
            face = Face.UP;
        } else {
            face = Face.DOWN;
        }
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

        DefaultCard card = (DefaultCard) o;

        if (!suit.equals(card.suit)) return false;
        return rank.equals(card.rank);
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }
}