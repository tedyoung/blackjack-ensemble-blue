package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PlayerAccount {
    private final List<PlayerAccountEvent> events = new ArrayList<>();
    private int balance = -99;

    public PlayerAccount(List<PlayerAccountEvent> events) {
        for (PlayerAccountEvent event: events) {
            this.events.add(event);

            if (event instanceof PlayerRegistered) {
                balance = 0;
            }

            if (event instanceof MoneyDeposited moneyDeposited) {
                apply(moneyDeposited);
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
        MoneyDeposited moneyDeposited = new MoneyDeposited(amount);
        events.add(moneyDeposited);
        apply(moneyDeposited);
    }

    private void apply(MoneyDeposited moneyDeposited) {
        balance += moneyDeposited.amount();
    }

    public Stream<PlayerAccountEvent> events() {
        return events.stream();
    }
}
