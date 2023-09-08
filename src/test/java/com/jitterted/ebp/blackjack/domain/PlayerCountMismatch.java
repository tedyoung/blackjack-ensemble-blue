package com.jitterted.ebp.blackjack.domain;

public class PlayerCountMismatch extends RuntimeException {
    public PlayerCountMismatch() {
        super();
    }

    public PlayerCountMismatch(String message) {
        super(message);
    }
}
