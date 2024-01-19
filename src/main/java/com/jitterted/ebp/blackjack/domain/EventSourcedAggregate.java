package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class EventSourcedAggregate {
    private PlayerId id;
    private final List<PlayerAccountEvent> events = new ArrayList<>();

    public EventSourcedAggregate(PlayerId playerId) {
        id = playerId;
    }

    public PlayerId getId() {
        return id;
    }

    public void setId(PlayerId playerId) {
        id = playerId;
    }

    public abstract void apply(PlayerAccountEvent event);

    protected void enqueue(PlayerAccountEvent event) {
        this.events.add(event);
        apply(event);
    }

    public Stream<PlayerAccountEvent> events() {
        return events.stream();
    }
}
