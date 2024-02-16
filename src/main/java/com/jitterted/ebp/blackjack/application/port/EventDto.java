package com.jitterted.ebp.blackjack.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerWonGame;

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

    public static EventDto createEventDto(int playerId, int eventId, PlayerAccountEvent event) {
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

    public PlayerWonGame createPlayerWonGameEvent() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, PlayerWonGame.class);
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

