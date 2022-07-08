package com.jitterted.ebp.blackjack.domain;

public enum Suit {
    HEARTS("♥", true),
    CLUBS("♣", false),
    DIAMONDS("♦", true),
    SPADES("♠", false);

    private final String displaySymbol;
    private final boolean isRed;

    Suit(String displaySymbol, boolean isRed) {
        this.displaySymbol = displaySymbol;
        this.isRed = isRed;
    }

    public String displaySymbol() {
        return displaySymbol;
    }

    public boolean isRed() {
        return isRed;
    }
}
