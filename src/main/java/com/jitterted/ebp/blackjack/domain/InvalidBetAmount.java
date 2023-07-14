package com.jitterted.ebp.blackjack.domain;

public class InvalidBetAmount extends RuntimeException {
    public InvalidBetAmount() {
        super();
    }

    public InvalidBetAmount(String message) {
        super(message);
    }
}
