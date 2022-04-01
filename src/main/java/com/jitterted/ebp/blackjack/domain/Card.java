package com.jitterted.ebp.blackjack.domain;

public interface Card {
    int rankValue();

    Suit suit();

    Rank rank();

    boolean isFaceDown();

    void flip();
}