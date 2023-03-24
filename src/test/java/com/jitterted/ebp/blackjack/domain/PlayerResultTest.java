package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerResultTest {

    @Test
    void resultReturnsOutcomeForPlayerBeatsDealer() {
        PlayerResult playerResult = new PlayerResult(new Player(1),
                                                     PlayerOutcome.PLAYER_BEATS_DEALER);

        assertThat(playerResult.bet())
                .isEqualTo(Bet.of(10));
        assertThat(playerResult.payout())
                .isEqualTo(20);
    }

    @Test
    void resultReturnsOutcomeForPlayerLoses() {
        PlayerResult playerResult = new PlayerResult(new Player(1),
                                                     PlayerOutcome.PLAYER_LOSES,
                                                     Bet.of(10));

        assertThat(playerResult.bet())
                .isEqualTo(Bet.of(10));
        assertThat(playerResult.payout())
                .isEqualTo(0);
    }

    @Test
    @Disabled // while we transition to new constructor
    void resultReturnsOutcomeForPlayerBeatsDealerForBet15() {
        PlayerResult playerResult = new PlayerResult(new Player(1),
                                                     PlayerOutcome.PLAYER_BEATS_DEALER);

        assertThat(playerResult.bet())
                .isEqualTo(Bet.of(15));
        assertThat(playerResult.payout())
                .isEqualTo(30);
    }
}