package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerResultTest {

    @Test
    void resultReturnsOutcomeForPlayerLoses() {
        PlayerResult playerResult = new PlayerResult(new PlayerInGame(PlayerId.of(1)),
                                                     PlayerOutcome.PLAYER_LOSES,
                                                     Bet.of(10));

        assertThat(playerResult.bet())
                .isEqualTo(Bet.of(10));
        assertThat(playerResult.payout())
                .isEqualTo(0);
    }

    @Test
    void resultReturnsOutcomeForPlayerBeatsDealer() {
        PlayerResult playerResult = new PlayerResult(new PlayerInGame(PlayerId.of(1)),
                                                     PlayerOutcome.PLAYER_BEATS_DEALER,
                                                     Bet.of(10));

        assertThat(playerResult.bet())
                .isEqualTo(Bet.of(10));
        assertThat(playerResult.payout())
                .isEqualTo(20);
    }

    @Test
    void resultReturnsOutcomeForPlayerBeatsDealerForBet15() {
        PlayerResult playerResult = new PlayerResult(new PlayerInGame(PlayerId.of(1)),
                                                     PlayerOutcome.PLAYER_BEATS_DEALER,
                                                     Bet.of(15));

        assertThat(playerResult.bet())
                .isEqualTo(Bet.of(15));
        assertThat(playerResult.payout())
                .isEqualTo(30);
    }

    @Test
    void payoffForPushIsOneToOne() {
        PlayerResult playerResult = new PlayerResult(new PlayerInGame(PlayerId.of(1)),
                                                     PlayerOutcome.PLAYER_PUSHES_DEALER,
                                                     Bet.of(17));

        assertThat(playerResult.payout())
                .isEqualTo(17);
    }

    @Test
    void payoffForBlackjackIsThreeToTwo() {
        PlayerResult playerResult = new PlayerResult(new PlayerInGame(PlayerId.of(1)),
                                                     PlayerOutcome.BLACKJACK,
                                                     Bet.of(20));

        assertThat(playerResult.payout())
                .isEqualTo(50);
    }

    @Test
    void payoffForDealerBustedIsTwoToOne() {
        PlayerResult playerResult = new PlayerResult(new PlayerInGame(PlayerId.of(1)),
                                                     PlayerOutcome.DEALER_BUSTED,
                                                     Bet.of(25));

        assertThat(playerResult.payout())
                .isEqualTo(50);
    }

    @Test
    public void payoffForPlayerBustedIsZero() throws Exception {
        PlayerResult playerResult = new PlayerResult(new PlayerInGame(PlayerId.of(1)),
                                                     PlayerOutcome.PLAYER_BUSTED,
                                                     Bet.of(13));

        assertThat(playerResult.payout())
                .isEqualTo(0);
    }
}