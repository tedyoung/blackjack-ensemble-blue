package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class EventSourcedAggregate {
    private int lastEventId;
    private PlayerId playerId;
    protected List<PlayerAccountEvent> freshEvents = new ArrayList<>();
    private boolean isLocked = false;

    public EventSourcedAggregate(PlayerId playerId, int lastEventId) {
        this.playerId = playerId;
        this.lastEventId = lastEventId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(PlayerId playerId) {
        this.playerId = playerId;
    }

    public abstract void apply(PlayerAccountEvent event);

    protected void enqueue(PlayerAccountEvent event) {
        if (isLocked) {
            throw new IllegalStateException("Aggregate is locked to prevent further changes (probably because it's been saved).");
        }
        freshEvents.add(event);
        apply(event);
    }

    public int lastEventId() {
        return lastEventId;
    }

    public Stream<PlayerAccountEvent> freshEvents() {
        return freshEvents.stream();
    }

    public void clearFreshEvents() {
        lastEventId += freshEvents.size();
        freshEvents.clear();
    }

    public void lock() {
        isLocked = true;
    }

    @Override
    public String toString() {
        return "EventSourcedAggregate{" +
                "playerId=" + playerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventSourcedAggregate that = (EventSourcedAggregate) o;
        if (playerId == null && that.playerId == null) {
            return false;
        }
        return Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }

}
