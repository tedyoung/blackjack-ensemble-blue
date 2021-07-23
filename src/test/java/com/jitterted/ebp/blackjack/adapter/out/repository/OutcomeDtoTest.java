package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.PlayerOutcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OutcomeDtoTest {

    @Test
    public void playerBustedAsString() throws Exception {
        assertOutcomeEnumAndString(PlayerOutcome.PLAYER_BUSTED, "Player Busted");
    }

    @Test
    public void dealerBustedAsString() throws Exception {
        assertOutcomeEnumAndString(PlayerOutcome.DEALER_BUSTED, "Dealer Busted");
    }

    @Test
    void playerLosesAsString() {
        assertOutcomeEnumAndString(PlayerOutcome.PLAYER_LOSES, "Player Loses");
    }

    @Test
    public void playerWinsBlackjack() throws Exception {
        assertOutcomeEnumAndString(PlayerOutcome.BLACKJACK, "Player Wins Blackjack");
    }

    @Test
    public void playerBeatsDealer() throws Exception {
        assertOutcomeEnumAndString(PlayerOutcome.PLAYER_BEATS_DEALER, "Player Beats Dealer");
    }

    @Test
    public void playerPushesDealer() throws Exception {
        assertOutcomeEnumAndString(PlayerOutcome.PLAYER_PUSHES_DEALER, "Player Pushes Dealer");
    }

    private void assertOutcomeEnumAndString(PlayerOutcome gameOutcome, String expected) {
        OutcomeDto outcomeDto = new OutcomeDto(gameOutcome);

        assertThat(outcomeDto.asString()).isEqualTo(expected);
    }
}
