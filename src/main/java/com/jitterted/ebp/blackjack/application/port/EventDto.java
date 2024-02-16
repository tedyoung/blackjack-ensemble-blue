package com.jitterted.ebp.blackjack.application.port;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jitterted.ebp.blackjack.domain.MoneyBet;
import com.jitterted.ebp.blackjack.domain.MoneyDeposited;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerLostGame;
import com.jitterted.ebp.blackjack.domain.PlayerRegistered;
import com.jitterted.ebp.blackjack.domain.PlayerWonGame;

import java.util.Map;

public class EventDto { // represents a row in a database table
    private final int playerId; // more generally, this is the Aggregate ID
    private final int eventId;
    private final String eventType;
    private final String json;
    private final Map<String, Class<? extends PlayerAccountEvent>> converters = Map.of(
            "MoneyBet", MoneyBet.class,
            "MoneyDeposited", MoneyDeposited.class,
            "PlayerLostGame", PlayerLostGame.class,
            "PlayerRegistered", PlayerRegistered.class,
            "PlayerWonGame", PlayerWonGame.class
    );
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    public static EventDto from(int playerId, int eventId, PlayerAccountEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(event);
            return new EventDto(playerId, eventId, event.getClass().getSimpleName(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getEventId() {
        return eventId;
    }

    public PlayerAccountEvent toDomain() {
        try {
            return objectMapper.readValue(json, converters.get(eventType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        return "EventDto: {playerId=%d, eventId=%d, eventType='%s', json='%s'}"
                .formatted(playerId, eventId, eventType, json);
    }
}

