package com.jitterted.ebp.blackjack.domain;

public enum Face {
    DOWN(true),
    UP(false);

    private boolean value;

    Face(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }
}