package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class EventSourcedAggregate<
        Id,
        Event,
        State extends EventSourcedAggregate.AggregateState<Event>
        > {

    private Id aggregateId;
    protected final State state;
    // instead of holding all events, only hold new events
    // also hold the last event ID so we can start incrementing from there for new events
    //  --> this lastEventId also represents the "version" of the Aggregate

    @Deprecated // Goal: get rid of events, using only freshEvents
    private final List<Event> events = new ArrayList<>();
    protected List<Event> freshEvents = new ArrayList<>();

    public EventSourcedAggregate(Id playerId, State initialState, List<Event> events) {
        state = initialState;
        this.aggregateId = playerId;
        for (Event event : events) {
            enqueue(event);
        }
        freshEvents.clear();
    }

    public Id getPlayerId() {
        return aggregateId;
    }

    public void setPlayerId(Id playerId) {
        this.aggregateId = playerId;
    }

    protected void enqueue(Event event) {
        this.freshEvents.add(event);
        this.events.add(event);
        this.state.apply(event);
    }

    @Deprecated
    public Stream<Event> events() {
        return events.stream();
    }

    public int lastEventId() {
        return events.size() - 1;
    }

    public Stream<Event> freshEvents() {
        return freshEvents.stream();
    }

    public interface AggregateState<Event> {
        void apply(Event event);
    }
}
