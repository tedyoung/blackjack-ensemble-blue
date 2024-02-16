package com.jitterted.ebp.blackjack.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jitterted.ebp.blackjack.domain.MoneyDeposited;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerOutcome;
import com.jitterted.ebp.blackjack.domain.PlayerRegistered;
import com.jitterted.ebp.blackjack.domain.PlayerWonGame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class EventDtoTest {

    // eventDto -> playerAccountEvent
    // playerAccountEvent -> eventDto

    //{"type":"com.jitterted.ebp.blackjack.domain.PlayerWonGame","payout":42,"playerOutcome":"DEALER_BUSTED"}

    @Test
    void givenPlayerRegisteredEventCreateEventDto() throws JsonProcessingException {
        PlayerRegistered event = new PlayerRegistered("Judy");

        EventDto eventDto = EventDto.from(17, 2, event);

        assertThat(eventDto)
                .isEqualTo(new EventDto(17, 2, "PlayerRegistered","""
                        {"name":"Judy"}"""));
    }

    @Test
    void givenPlayerWonGameEventCreateEventDto() throws JsonProcessingException {
        PlayerWonGame event = new PlayerWonGame(42, PlayerOutcome.DEALER_BUSTED);

        EventDto eventDto = EventDto.from(3, 14, event);

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
        EventDto eventDto = EventDto.from(3, 14, expected);

        PlayerWonGame actual = (PlayerWonGame) eventDto.toDomain();

        assertThat(actual)
                .isEqualTo(expected);
    }

    @Test
    void moneyDepositedEventConvertedFromJson() {
        MoneyDeposited expected = new MoneyDeposited(55);
        EventDto eventDto = EventDto.from(3, 14, expected);

        MoneyDeposited actual = (MoneyDeposited) eventDto.toDomain();

        assertThat(actual)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("events")
    void moneyBetEventConvertedFromJson(PlayerAccountEvent sourceEvent) {
        EventDto eventDto = EventDto.from(3, 14, sourceEvent);

        PlayerAccountEvent actual = eventDto.toDomain();

        assertThat(actual)
                .isEqualTo(sourceEvent);
    }

    public static Stream<Arguments> events() {
        return Stream.of(
                Arguments.of(new MoneyDeposited(55))
        );
    }
}
