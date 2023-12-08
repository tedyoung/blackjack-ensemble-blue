package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount {
    private int balance = 0;

    public PlayerAccount(PlayerId playerId) {

    }

    public PlayerAccount(PlayerId playerId, List<PlayerAccountEvent> events) {
        for (PlayerAccountEvent event : events) {
            if (event instanceof MoneyDeposited moneyDeposited) {
                balance = moneyDeposited.amount();
            }
        }
    }

    public int balance() {
        return balance;
    }

    public void deposit(int amount) {

    }
}
