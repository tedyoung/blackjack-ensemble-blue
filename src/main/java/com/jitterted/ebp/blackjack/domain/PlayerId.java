package com.jitterted.ebp.blackjack.domain;

public record PlayerId(int id) {
    public static PlayerId of(int id) {
        return new PlayerId(id);
    }
}
