package com.jitterted.ebp.blackjack.domain;

public class MoneyDeposited {
    private final int money;

    public MoneyDeposited(int money) {
        this.money = money;
    }

    public int amount() {
        return money;
    }
}
