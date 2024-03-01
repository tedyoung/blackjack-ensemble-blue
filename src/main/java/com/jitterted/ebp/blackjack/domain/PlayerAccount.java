package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerAccount extends EventSourcedAggregate {
    private int balance = -1;
    private String name = "Matilda";

    // create factory method for Repository usage, then make this private
    public PlayerAccount(PlayerId playerId, List<PlayerAccountEvent> events) {
        super(playerId);
        for (PlayerAccountEvent event : events) {
            enqueue(event);
        }
        freshEvents.clear();
    }

    public static PlayerAccount register(String name) {
        // create PlayerAccount with no events
        // call registerPlayer, which needs to create and enqueue the PlayerRegistered event
        PlayerRegistered playerRegistered = new PlayerRegistered(name);
        return new PlayerAccount(null, List.of(playerRegistered));
    }

    @Override
    public void apply(PlayerAccountEvent event) {
        switch (event) {
            case PlayerRegistered(String name) -> registerPlayer(name);
            case MoneyDeposited(int amount) -> balance += amount;
            case MoneyBet(int amount) -> balance -= amount;
            case PlayerWonGame(int payout, PlayerOutcome ignore) -> balance += payout;
            case PlayerLostGame ignore -> {}
        }
    }

    private void registerPlayer(String name) {
        this.name = name;
        this.balance = 0;
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
