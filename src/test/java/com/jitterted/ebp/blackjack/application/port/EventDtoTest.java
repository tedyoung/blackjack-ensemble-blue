package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerRegistered;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventDtoTest {
    // eventDto -> playerAccountEvent
    // playerAccountEvent -> eventDto


    @Test
    void givenPlayerAccountEventCreateEventDto() {
        PlayerAccountEvent event = new PlayerRegistered("Judy");

    }
}