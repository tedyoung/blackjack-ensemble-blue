package com.jitterted.ebp.blackjack.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerRegistered;
import com.jitterted.ebp.blackjack.domain.PlayerWonGame;

import java.util.Map;
import java.util.Objects;

public class EventDto { // represents a row in a database table
    private final int playerId; // more generally, this is the Aggregate ID
    private final int eventId;
    private final String eventType;
    private final String json;

    public EventDto(int playerId, int eventId, String eventType, String json) {
        this.playerId = playerId;
        this.eventId = eventId;
        this.eventType = eventType;
        this.json = json;
    }

    /*
        Table draft:

        PK PlayerId-EventId
           JSON String eventContent

        PlayerID | EventId  | EventType         |  JSON Content
        -----------------------------------------------------------------
        0       | 0         | PlayerRegistered  | { name: "Judy" }
        0       | 1         | MoneyDeposited    | { amount: 10 }
    */

    public static EventDto from(int playerId, int eventId, PlayerRegistered event) {
        return new EventDto(playerId, eventId, "PlayerRegistered", String.format(
                "{\"name\": \"%s\"}",
                event.name()));
    }

    public static EventDto from(int playerId, int eventId, PlayerWonGame event) {
        return new EventDto(playerId, eventId, "PlayerWonGame", String.format(
                "{\"payout\":%s,\"playerOutcome\":\"%s\"}",
                event.payout(), event.playerOutcome()));
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getEventId() {
        return eventId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDto eventDto = (EventDto) o;

        if (playerId != eventDto.playerId) return false;
        if (eventId != eventDto.eventId) return false;
        if (!eventType.equals(eventDto.eventType)) return false;
        return json.equals(eventDto.json);
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + eventId;
        result = 31 * result + eventType.hashCode();
        result = 31 * result + json.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "playerId=" + playerId +
                ", eventId=" + eventId +
                ", eventType='" + eventType + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}

