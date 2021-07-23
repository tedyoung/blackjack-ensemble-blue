package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerTest {

    @Test
    public void newPlayerIsNotDone() throws Exception {
        Player player = new Player();

        assertThat(player.isDone())
                .isFalse();
    }
}