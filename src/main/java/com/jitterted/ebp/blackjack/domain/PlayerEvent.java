package com.jitterted.ebp.blackjack.domain;

import java.util.Objects;

public class PlayerEvent {

    private final int playerId;
    private final String reasonDone;

    public PlayerEvent(int playerId, String reasonDone) {
        this.playerId = playerId;
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
        return playerId == that.playerId && reasonDone.equals(that.reasonDone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, reasonDone);
    }

    @Override
    public String toString() {
        return "PlayerEvent{" +
                "playerId=" + playerId +
                ", reasonDone='" + reasonDone + '\'' +
                '}';
    }

}