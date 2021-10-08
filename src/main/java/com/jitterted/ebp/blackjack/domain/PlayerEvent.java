package com.jitterted.ebp.blackjack.domain;

import java.util.Objects;

public class PlayerEvent {

    private final String reasonDone;

    public PlayerEvent(String reasonDone) {
        this.reasonDone = reasonDone;
    }

    public String reasonDone() {
        return reasonDone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerEvent that = (PlayerEvent) o;
        return reasonDone.equals(that.reasonDone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reasonDone);
    }
}