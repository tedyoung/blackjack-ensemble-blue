package com.jitterted.ebp.blackjack.domain;

public class InsufficientBalance extends RuntimeException {

    public InsufficientBalance(String message) {
        super(message);
    }
}
