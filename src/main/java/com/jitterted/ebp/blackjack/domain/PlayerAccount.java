package com.jitterted.ebp.blackjack.domain;

import java.util.Collections;
import java.util.List;

public class PlayerAccount extends EventSourcedAggregate<
        PlayerId,
        PlayerAccountEvent,
        PlayerAccount.State> {

    private PlayerAccount() {
        this(null, Collections.emptyList());
    }

    private PlayerAccount(PlayerId playerId, List<PlayerAccountEvent> events) {
        super(playerId, new State(), events);
    }

    public static PlayerAccount register(String name) {
        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.registerPlayer(name);
        return playerAccount;
    }

    public static PlayerAccount reconstitute(PlayerId playerId, List<PlayerAccountEvent> events) {
        requiresPlayerId(playerId);
        return new PlayerAccount(playerId, events);
    }

    private static void requiresPlayerId(PlayerId playerId) {
        if (playerId == null) {
            throw new IllegalArgumentException("reconstitute must have playerId");
        }
    }

    private void registerPlayer(String name) {
        PlayerRegistered playerRegistered = new PlayerRegistered(name);
        enqueue(playerRegistered);
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

    public String name() {
        return state.name;
    }

    public int balance() {
        return state.balance;
    }

    public static class State implements AggregateState<PlayerAccountEvent> {
        int balance = -1;
        String name = "DEFAULT NAME";

        public void apply(PlayerAccountEvent event) {
            switch (event) {
                case PlayerRegistered(String playerName) -> {
                    this.name = playerName;
                    this.balance = 0;
                }
                case MoneyDeposited(int amount) -> balance += amount;
                case MoneyBet(int amount) -> balance -= amount;
                case PlayerWonGame(int payout, PlayerOutcome ignore) -> balance += payout;
                case PlayerLostGame ignore -> {
                }
            }
        }
    }
}
