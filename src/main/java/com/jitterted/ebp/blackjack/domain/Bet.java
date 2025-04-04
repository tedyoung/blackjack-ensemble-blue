package com.jitterted.ebp.blackjack.domain;

public record Bet(int amount) {

    public Bet {
        if (invalid(amount)) {
            throw new InvalidBetAmount("Bet amount: %d is not within 1 to 100".formatted(amount));
        }
    }

    private static boolean invalid(int amount) {
        return amount <= 0 || amount > 100;
    }

    public static Bet of(int amount) {
        return new Bet(amount);
    }

    public static boolean isValidAmount(int amount) {
        return false;
    }
}
