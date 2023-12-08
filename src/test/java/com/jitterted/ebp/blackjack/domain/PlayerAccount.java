package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount {
    private final List<PlayerAccountEvent> events;
    private int balance = -99;

    public PlayerAccount(List<PlayerAccountEvent> events) {
        this.events = events;
        for (PlayerAccountEvent event : events) {
            if (event instanceof PlayerRegistered) {
                balance = 0;
            }

            if (event instanceof MoneyDeposited moneyDeposited) {
                balance = moneyDeposited.amount();
            }
        }
    }

    public static PlayerAccount register() {
        return new PlayerAccount(List.of(new PlayerRegistered()));
    }

    public int balance() {
        return balance;
    }

    public void deposit(int amount) {

    }

    public List<PlayerAccountEvent> events() {
        return events;
    }
}
