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
            case PlayerWonGame(int payout, _) -> balance += payout;
            case PlayerLostGame _ -> {}
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
        MoneyBet event = new MoneyBet(amount);
        enqueue(event);
    }

    public void deposit(int amount) {
        MoneyDeposited event = new MoneyDeposited(amount);
        enqueue(event);
    }

    public void win(int payout, PlayerOutcome playerOutcome) {
        PlayerWonGame event = new PlayerWonGame(payout, playerOutcome);
        enqueue(event);
    }

    public void lose(PlayerOutcome playerOutcome) {
        PlayerLostGame event = new PlayerLostGame(playerOutcome);
        enqueue(event);
    }


}
