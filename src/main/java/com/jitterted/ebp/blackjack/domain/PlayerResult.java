package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerResult {

    private final int id;
    private final List<Card> cards;
    private final PlayerOutcome outcome;
    private final Bet bet;

    @Deprecated // prefer to use more complete constructor below
    public PlayerResult(Player player, PlayerOutcome outcome) {
        this(player, outcome, Bet.of(1));
    }

    public PlayerResult(Player player, PlayerOutcome outcome, Bet bet) {
        this.id = player.id();
        this.cards = player.cards();
        this.outcome = outcome;
        this.bet = bet;
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

    public Bet bet() {
        return bet;
    }

    public int payout() {
        return outcome.payoff(bet);
    }

}
