package com.jitterted.ebp.blackjack.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jitterted.ebp.blackjack.domain.PlayerOutcome;
import com.jitterted.ebp.blackjack.domain.PlayerRegistered;
import com.jitterted.ebp.blackjack.domain.PlayerWonGame;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EventDtoTest {

    // eventDto -> playerAccountEvent
    // playerAccountEvent -> eventDto

    //{"type":"com.jitterted.ebp.blackjack.domain.PlayerWonGame","payout":42,"playerOutcome":"DEALER_BUSTED"}

    @Test
    void givenPlayerRegisteredEventCreateEventDto() throws JsonProcessingException {
        PlayerRegistered event = new PlayerRegistered("Judy");

        EventDto eventDto = EventDto.createEventDto(17, 2, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(17, 2, "PlayerRegistered","""
                        {"name":"Judy"}"""));
    }

    @Test
    void givenPlayerWonGameEventCreateEventDto() throws JsonProcessingException {
        PlayerWonGame event = new PlayerWonGame(42, PlayerOutcome.DEALER_BUSTED);

        EventDto eventDto = EventDto.createEventDto(3, 14, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(
                        3,
                        14,
                        "PlayerWonGame",
                        """
                        {"payout":42,"playerOutcome":"DEALER_BUSTED"}"""));

    }

    @Test
    void convertEventsDtoToAThingAndLookAtIt() throws Exception {
        PlayerWonGame expected = new PlayerWonGame(42, PlayerOutcome.DEALER_BUSTED);
        EventDto eventDto = EventDto.createEventDto(3, 14, expected);

        // convert back
        PlayerWonGame actual = eventDto.createPlayerWonGameEvent();

        // assert it equals event
        assertThat(actual)
                .isEqualTo(expected);

        System.out.println(eventDto);
        // EventDto: {playerId=3, eventId=14, eventType='PlayerWonGame', json='{"payout":42,"playerOutcome":"DEALER_BUSTED"}'}
    }
}
