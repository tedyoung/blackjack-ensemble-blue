package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class EventSourcedAggregate {
    private PlayerId playerId;
    // instead of holding all events, only hold new events
    // also hold the last event ID so we can start incrementing from there for new events
    //  --> this lastEventId also represents the "version" of the Aggregate

    // this will become only freshEvents and not All events
    private final List<PlayerAccountEvent> events = new ArrayList<>();
    private List<PlayerAccountEvent> freshEvents = new ArrayList<>();

    public EventSourcedAggregate(PlayerId playerId) {
        this.playerId = playerId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(PlayerId playerId) {
        this.playerId = playerId;
    }

    public abstract void apply(PlayerAccountEvent event);

    protected void enqueue(PlayerAccountEvent event) {
        this.freshEvents.add(event);
        this.events.add(event);
        apply(event);
    }

    public Stream<PlayerAccountEvent> events() {
        return events.stream();
    }

    public int lastEventId() {
        return events.size() - 1;
    }

    public Stream<PlayerAccountEvent> freshEvents() {
        return freshEvents.stream();
    }
}
