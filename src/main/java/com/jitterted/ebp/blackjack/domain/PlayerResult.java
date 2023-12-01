package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class PlayerResult {

    private final PlayerId playerId;
    private final List<Card> cards;
    private final PlayerOutcome outcome;
    private final Bet bet;

    public PlayerResult(PlayerInGame player, PlayerOutcome outcome, Bet bet) {
        this.playerId = player.playerId();
        this.cards = player.cards();
        this.outcome = outcome;
        this.bet = bet;
    }

    public PlayerOutcome outcome() {
        return outcome;
    }

    public PlayerId playerId() {
        return playerId;
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
