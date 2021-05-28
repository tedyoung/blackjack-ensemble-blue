package com.jitterted.ebp.blackjack.adapter.out.repository;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OutcomeDtoTest {

    @Test
    void playerLosesAsString() {
        OutcomeDto outcomeDto = new OutcomeDto();

        assertThat(outcomeDto.asString()).isEqualTo("Player Loses");
    }
}
