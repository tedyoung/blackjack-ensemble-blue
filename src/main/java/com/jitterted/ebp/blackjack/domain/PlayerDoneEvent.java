package com.jitterted.ebp.blackjack.domain;

import java.util.Objects;

public class PlayerDoneEvent {

    private final int playerId;
    private final PlayerReasonDone reasonDone;

    public PlayerDoneEvent(int playerId, PlayerReasonDone reasonDone) {
        this.playerId = playerId;
        this.reasonDone = reasonDone;
    }

    public int id() {
        return playerId;
    }

    public PlayerReasonDone reasonDone() {
        return reasonDone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDoneEvent that = (PlayerDoneEvent) o;
        return playerId == that.playerId && reasonDone == that.reasonDone;
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