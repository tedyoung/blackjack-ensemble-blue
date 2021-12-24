package com.jitterted.ebp.blackjack.domain;

public class PlayerResult {

    private PlayerOutcome outcome;

    public PlayerResult(PlayerOutcome outcome) {
        this.outcome = outcome;
    }

    public PlayerOutcome outcome() {
        return outcome;
    }
}
