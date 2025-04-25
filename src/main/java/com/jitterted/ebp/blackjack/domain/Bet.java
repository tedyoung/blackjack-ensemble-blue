package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.Result;

import java.util.Objects;

public final class Bet {
    private final int amount;

    private Bet(int amount) {
        this.amount = amount;
    }

    public static Bet of(int amount) {
        Result<Bet> result = create(amount);
        if (result.isFailure()) {
            throw new InvalidBetAmount(result.failureMessages().getFirst());
        }
        return result.values().getFirst();
    }

    public static boolean isValid(int amount) {
        return amount > 0 && amount <= 100;
    }

    public static Result<Bet> create(int betAmount) {
        return isValid(betAmount)
                ? Result.success(new Bet(betAmount))
                : Result.failure("Bet amount: %d is not within 1 to 100".formatted(betAmount));
    }

    public int amount() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Bet) obj;
        return this.amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "Bet[" +
                "amount=" + amount + ']';
    }

}
