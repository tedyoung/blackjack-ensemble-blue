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
    private final String json;

    public EventDto(int playerId, int eventId, String json) {
        this.playerId = playerId;
        this.json = json;
        this.eventId = eventId;
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
        return new EventDto(playerId, eventId, String.format(
                "{\"type\": \"%s\", \"name\": \"%s\"}",
                event.getClass().getSimpleName(),
                event.name()));
    }

    public static EventDto from(int playerId, int eventId, PlayerWonGame event) {
        var json = "{\"type\": \"%s\", \"payout\": %d, \"playerOutcome\": \"%s\"}"
                .formatted(event.getClass().getSimpleName(), event.payout(), event.playerOutcome());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.convertValue(event, new TypeReference<>() {
            });
            map.put("type", event.getClass().getSimpleName());
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new EventDto(playerId, eventId, json);
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
        return Objects.equals(json, eventDto.json);
    }

    @Override
    public int hashCode() {
        int result = playerId;
        result = 31 * result + eventId;
        result = 31 * result + (json != null ? json.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "playerId=" + playerId +
                ", eventId=" + eventId +
                ", json='" + json + '\'' +
                '}';
    }
}

