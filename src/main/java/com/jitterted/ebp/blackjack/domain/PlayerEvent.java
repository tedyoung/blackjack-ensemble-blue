package com.jitterted.ebp.blackjack.domain;

public class PlayerEvent {

    private final String reasonDone;

    public PlayerEvent(String reasonDone) {
        this.reasonDone = reasonDone;
    }

    public String reasonDone() {
        return reasonDone;
    }
}