package com.jitterted.ebp.blackjack.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jitterted.ebp.blackjack.domain.MoneyDeposited;
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
    void playerWonEventConvertedFromJson() throws Exception {
        PlayerWonGame expected = new PlayerWonGame(42, PlayerOutcome.DEALER_BUSTED);
        EventDto eventDto = EventDto.createEventDto(3, 14, expected);

        PlayerWonGame actual = eventDto.createPlayerWonGameEvent();

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void moneyDepositedEventConvertedFromJson() {
        MoneyDeposited expected = new MoneyDeposited(55);
        EventDto eventDto = EventDto.createEventDto(3, 14, expected);

        MoneyDeposited actual = eventDto.createPlayerWonGameEvent();

        assertThat(actual)
                .isEqualTo(expected);
    }
}
