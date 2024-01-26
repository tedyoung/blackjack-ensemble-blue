package com.jitterted.ebp.blackjack.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerRegistered;
import com.jitterted.ebp.blackjack.domain.PlayerWonGame;

import java.util.Objects;

public class EventDto {
    private final int playerId;
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

        PlayerID | EventId   |   Json
        -----------------------------------------------------------------
        0       | 0          | { type: "PlayerRegistered", name: "Judy"}
        0       | 1          | { type: "MoneyDeposited", amount: 10}
    */

    public static EventDto from(int playerId, int eventId, PlayerRegistered event) {
        return new EventDto(playerId, eventId, STR."""
        {"type": "\{event.getClass().getSimpleName()}", "name": "\{event.name()}"}\
        """);
    }

    public static EventDto from(int playerId, int eventId, PlayerWonGame event) {
        var json = STR."""
                {"type": "\{event.getClass().getSimpleName()}", \
                "payout": \{event.payout()}, \
                "playerOutcome": "\{event.playerOutcome()}"}\
                """;

        ObjectMapper objectMapper = new ObjectMapper();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType(PlayerAccountEvent.class)
                        .build();
        objectMapper.activateDefaultTyping(ptv);
        try {
            json = objectMapper.writeValueAsString((PlayerAccountEvent) event);
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
