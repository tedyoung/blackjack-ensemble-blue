package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class EventSourcedAggregate {
    private final int lastEventId;
    private PlayerId playerId;
    protected List<PlayerAccountEvent> freshEvents = new ArrayList<>();

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
        freshEvents.add(event);
        apply(event);
    }

    public int lastEventId() {
        return lastEventId;
    }

    public Stream<PlayerAccountEvent> freshEvents() {
        return freshEvents.stream();
    }
}
