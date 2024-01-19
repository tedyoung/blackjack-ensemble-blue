package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerRegistered;

import java.util.Objects;

public class EventDto {
    private final String json;

    public EventDto(String json) {
        this.json = json;
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

    public static EventDto from(PlayerRegistered event) {
        return new EventDto(STR."""
        {"name": "\{ event.name() }"}\
        """);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDto eventDto = (EventDto) o;

        return Objects.equals(json, eventDto.json);
    }

    @Override
    public int hashCode() {
        return json != null ? json.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EventDto[" +
                "json='" + json + '\'' +
                ']';
    }
}
