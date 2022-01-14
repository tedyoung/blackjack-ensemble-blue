package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerResult {

    private final int id;
    private final List<Card> cards;
    private final PlayerOutcome outcome;

    public PlayerResult(Player player, PlayerOutcome outcome) {
        this.id = player.id();
        this.cards = player.cards();
        this.outcome = outcome;
    }

    public PlayerOutcome outcome() {
        return outcome;
    }

    public int id() {
        return id;
    }

    public List<Card> cards() {
        return cards;
    }
}
