package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount extends EventSourcedAggregate {
    private int balance = -99;

    public PlayerAccount(List<PlayerAccountEvent> events) {
        for (PlayerAccountEvent event: events) {
            enqueue(event);
        }
    }

    @Override
    public void apply(PlayerAccountEvent event) {
        if (event instanceof PlayerRegistered) {
            balance = 0;
        }

        if (event instanceof MoneyDeposited moneyDeposited) {
            balance += moneyDeposited.amount();
        }
    }

    public static PlayerAccount register(String name) {
        return new PlayerAccount(List.of(new PlayerRegistered(name)));
    }

    public int balance() {
        return balance;
    }

    public void deposit(int amount) {
        PlayerAccountEvent event = new MoneyDeposited(amount);
        enqueue(event);
    }

    public String name() {
        return null;
    }
}
