package com.jitterted.ebp.blackjack.domain;

import java.util.Collections;
import java.util.List;

public class PlayerAccount extends EventSourcedAggregate {
    private int balance = -1;
    private String name = "Matilda";

    // TODO: create factory method for Repository-only usage ("reconstitute")
    // then make this private
    public PlayerAccount(PlayerId playerId, List<PlayerAccountEvent> events) {
        super(playerId);
        for (PlayerAccountEvent event : events) {
            enqueue(event);
        }
        freshEvents.clear();
    }

    public static PlayerAccount register(String name) {
        PlayerAccount playerAccount = new PlayerAccount(null, Collections.emptyList());
        playerAccount.registerPlayer(name);
        return playerAccount;
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
            case PlayerWonGame(int payout, PlayerOutcome ignore) -> balance += payout;
            case PlayerLostGame ignore -> {
            }
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
        return name;
    }

    public int balance() {
        return balance;
    }
}
