package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        List<PlayerAccountEvent> inputEvents = new ArrayList<>();
        inputEvents.add(new PlayerRegistered());
        return new PlayerAccount(inputEvents);
    }

    public int balance() {
        return balance;
    }

    public void deposit(int amount) {
        MoneyDeposited moneyDeposited = new MoneyDeposited(amount);
        events.add(moneyDeposited);
    }

    public Stream<PlayerAccountEvent> events() {
        return events.stream();
    }
}
