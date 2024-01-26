package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerRegistered;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EventDtoTest {
    // eventDto -> playerAccountEvent
    // playerAccountEvent -> eventDto


    @Test
    void givenPlayerRegisteredEventCreateEventDto() {
        PlayerRegistered event = new PlayerRegistered("Judy");

        EventDto eventDto = EventDto.from(2, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto("""
                                                {"type": "PlayerRegistered", "name": "Judy"}"""));
        assertThat(eventDto.getEventId())
                .isEqualTo(2);
        // TODO: add assertions about Player ID and Event ID
    }
}