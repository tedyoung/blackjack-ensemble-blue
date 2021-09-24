package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class GameEventTest {

    @Test
    public void initialDealHasNoEvents() throws Exception {
        Game game = new Game(StubDeck);

        game.initialDeal();

        assertThat(game.events())
                .isEmpty();
    }

}