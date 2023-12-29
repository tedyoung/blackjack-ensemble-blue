package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount extends EventSourcedAggregate {
    private int balance = -1;
    private String name = "Matilda";

    public PlayerAccount(List<PlayerAccountEvent> events) {
        for (PlayerAccountEvent event : events) {
            enqueue(event);
        }
    }

    @Override
    public void apply(PlayerAccountEvent event) {
        switch (event) {
            case PlayerRegistered(String name) -> registerPlayer(name);
            case MoneyDeposited(int amount) -> balance += amount;
            case MoneyBet(int amount) -> balance -= amount;
        }
    }

    private void registerPlayer(String name) {
        this.name = name;
        this.balance = 0;
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
