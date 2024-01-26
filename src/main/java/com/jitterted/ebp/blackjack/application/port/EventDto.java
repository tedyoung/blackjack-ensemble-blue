package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerRegistered;

import java.util.Objects;

public class EventDto {
    private int eventId;
    private final String json;

    public EventDto(int eventId, String json) {
        this.json = json;
        this.eventId = eventId;
    }

    /*
        Table draft:

        PK PlayerId-EventId
           JSON String eventContent

        PlayerID | EventId   |   Json
        -----------------------------------------------------------------
        0       | 0          | { type: "PlayerRegistered", name: "Judy"}
        0       | 1          | { type: "MoneyDeposited", amount: 10}
    */

    public static EventDto from(int eventId, PlayerRegistered event) {
        return new EventDto(eventId, STR."""
        {"type": "\{ event.getClass().getSimpleName() }", "name": "\{ event.name() }"}\
        """);
    }

    public int getEventId() {
        return eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDto eventDto = (EventDto) o;

        if (eventId != eventDto.eventId) return false;
        return json.equals(eventDto.json);
    }

    @Override
    public int hashCode() {
        int result = json.hashCode();
        result = 31 * result + eventId;
        return result;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "eventId=" + eventId +
                ", json='" + json + '\'' +
                '}';
    }
}

