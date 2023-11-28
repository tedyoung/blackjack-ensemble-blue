package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Rank;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CustomDeckParserTest {

    @Test
    void rankQIsMappedToQueen() throws Exception {
        Rank rank = CustomDeckParser.rankFromString("Q");

        assertThat(rank)
                .isEqualByComparingTo(Rank.QUEEN);
    }

    @Test
    void lowercaseIsMappedToRank() throws Exception {
        Rank rank = CustomDeckParser.rankFromString("j");

        assertThat(rank)
                .isEqualByComparingTo(Rank.JACK);
    }
}