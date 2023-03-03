package com.jitterted.ebp.blackjack.domain;

public class Bet {

    private final int amount;

    public Bet(int amount) {
        if (amount <= 0 || amount > 100) {
            throw new InvalidBetAmount();
        }
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }

}
