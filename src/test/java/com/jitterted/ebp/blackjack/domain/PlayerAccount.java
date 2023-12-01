package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount {
    private int balance = 0;

    public PlayerAccount(PlayerId playerId) {

    }

    public PlayerAccount(PlayerId playerId, List<MoneyDeposited> events) {
        for (MoneyDeposited event : events) {
            balance = event.amount();
        }
    }

    public int balance() {
        return balance;
    }

    public void deposit(int amount) {

    }
}
