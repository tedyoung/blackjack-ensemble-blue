package com.jitterted.ebp.blackjack.domain;

public record Bet(int amount) {

    public Bet {
        if (amount <= 0 || amount > 100) {
            throw new InvalidBetAmount("Bet amount: %d is not within 1 to 100".formatted(amount));
        }
    }

    public static Bet of(int amount) {
        return new Bet(amount);
    }
}
