package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount extends EventSourcedAggregate {
    private int balance = -99;
    private String name = "Matilda";

    public PlayerAccount(List<PlayerAccountEvent> events) {
        for (PlayerAccountEvent event: events) {
            enqueue(event);
        }
    }

    @Override
    public void apply(PlayerAccountEvent event) {
        switch (event) {
            case PlayerRegistered playerRegistered -> {
                name = playerRegistered.name();
                balance = 0;
            }
            case MoneyDeposited moneyDeposited -> balance += moneyDeposited.amount();
            case MoneyBet moneyBet -> balance -= moneyBet.amount();
        }
    }

    public static PlayerAccount register(String name) {
        return new PlayerAccount(List.of(new PlayerRegistered(name)));
    }

    public String name() {
        return name;
    }

    public int balance() {
        return balance;
    }

    public void bet(int amount) {
        enqueue(new MoneyBet(amount));
    }

    public void deposit(int amount) {
        PlayerAccountEvent event = new MoneyDeposited(amount);
        enqueue(event);
    }
}
