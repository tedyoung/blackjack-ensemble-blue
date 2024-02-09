package com.jitterted.ebp.blackjack.application.port;

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
    void givenPlayerRegisteredEventCreateEventDto() {
        PlayerRegistered event = new PlayerRegistered("Judy");

        EventDto eventDto = EventDto.from(17, 2, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(17, 2, "PlayerRegistered","""
                        {"name": "Judy"}"""));
    }

    @Test
    void givenPlayerWonGameEventCreateEventDto() {
        PlayerWonGame event = new PlayerWonGame(42, PlayerOutcome.DEALER_BUSTED);

        EventDto eventDto = EventDto.from(3, 14, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(3, 14, """
                        {"payout":42,"playerOutcome":"DEALER_BUSTED","type":"PlayerWonGame"}"""));

    }
}
