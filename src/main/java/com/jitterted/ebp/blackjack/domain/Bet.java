package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.Result;

public record Bet(int amount) {

    public Bet {
        if (!isValid(amount)) {
            throw new InvalidBetAmount("Bet amount: %d is not within 1 to 100".formatted(amount));
        }
    }

    public static Bet of(int amount) {
        return new Bet(amount);
    }

    public static boolean isValid(int amount) {
        return amount > 0 && amount <= 100;
    }

    public static Result<Bet> create(int betAmount) {
        Result<Void> voidResult = validate(betAmount);
        return voidResult.isSuccess()
                ? Result.success(new Bet(betAmount))
                : Result.failure(voidResult.failureMessages().getFirst());
    }

    public static Result<Void> validate(int betAmount) {
        return isValid(betAmount)
                ? Result.success(null)
                : Result.failure("Bet amount: %d is not within 1 to 100".formatted(betAmount));
    }
}
