package com.jitterted.ebp.blackjack.domain;

import java.util.Collections;
import java.util.List;

public class PlayerAccount extends EventSourcedAggregate {
    private int balance = -1;
    private String name = "DEFAULT NAME";

    private PlayerAccount() {
        this(null, Collections.emptyList());
    }

    private PlayerAccount(PlayerId playerId, List<PlayerAccountEvent> events) {
        super(playerId, events.size());
        for (PlayerAccountEvent event : events) {
            apply(event);
        }
    }

    public static PlayerAccount register(String name) {
        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.registerPlayer(name);
        return playerAccount;
    }

    @Reconstitute
    public static PlayerAccount reconstitute(PlayerId playerId, List<PlayerAccountEvent> events) {
        requiresPlayerId(playerId);
        return new PlayerAccount(playerId, events);
    }

    private static void requiresPlayerId(PlayerId playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("reconstitute must have playerId");
        }
    }

    @Override
    public void apply(PlayerAccountEvent event) {
        switch (event) {
            case PlayerRegistered(String playerName) -> {
                this.name = playerName;
                this.balance = 0;
            }
            case MoneyDeposited(int amount) -> balance += amount;
            case MoneyBet(int amount) -> balance -= amount;
            case PlayerWonGame(int payout, _) -> balance += payout;
            case PlayerLostGame _ -> {
            }
        }
    }

    private void registerPlayer(String name) {
        PlayerRegistered playerRegistered = new PlayerRegistered(name);
        enqueue(playerRegistered);
    }

    public void bet(int amount) {
        if (balance < amount) {
            throw new InsufficientBalance("Player bet of $13 is more than available balance of $0");
        }
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

    public String name() {
        return name;
    }

    public int balance() {
        return balance;
    }

    @Override
    public String toString() {
        return "PlayerAccount{" +
                "id=" + getPlayerId() +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                '}';
    }
}
