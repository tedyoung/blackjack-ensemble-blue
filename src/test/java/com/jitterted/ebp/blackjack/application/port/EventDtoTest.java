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

        EventDto eventDto = EventDto.from(2, event, 17);

        assertThat(eventDto)
                .isEqualTo(new EventDto(17, 2, """
                                                {"type": "PlayerRegistered", "name": "Judy"}"""));
        // TODO: add assertions about Player ID
        assertThat(eventDto.getPlayerId())
                .isEqualTo(17);
    }
}