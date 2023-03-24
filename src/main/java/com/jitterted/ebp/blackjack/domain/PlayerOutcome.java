package com.jitterted.ebp.blackjack.domain;

public enum PlayerOutcome {
    PLAYER_BUSTED(0),
    DEALER_BUSTED(2),
    PLAYER_BEATS_DEALER(2),
    PLAYER_PUSHES_DEALER(1),
    PLAYER_LOSES(0),
    BLACKJACK(2.5);

    private final double payoff;

    PlayerOutcome(double payoff) {
        this.payoff = payoff;
    }

    int payoff(Bet bet) {
        return (int) (bet.amount() * payoff);
    }
}
