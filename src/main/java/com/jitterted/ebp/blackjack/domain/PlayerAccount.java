package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount extends EventSourcedAggregate<PlayerAccountEvent> {
    private int balance = -99;

    public PlayerAccount(List<PlayerAccountEvent> events) {
        for (PlayerAccountEvent event: events) {
            apply(event);
        }
    }

    @Override
    public void apply(PlayerAccountEvent event) {
        append(event);

        if (event instanceof PlayerRegistered) {
            balance = 0;
        }

        if (event instanceof MoneyDeposited moneyDeposited) {
            balance += moneyDeposited.amount();
        }
    }

    public static PlayerAccount register() {
        return new PlayerAccount(List.of(new PlayerRegistered()));
    }

    public int balance() {
        return balance;
    }

    public void deposit(int amount) {
        apply(new MoneyDeposited(amount));
    }

}
