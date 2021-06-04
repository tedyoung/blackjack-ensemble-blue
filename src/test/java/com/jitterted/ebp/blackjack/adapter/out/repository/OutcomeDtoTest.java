package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.GameOutcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OutcomeDtoTest {

    @Test
    public void playerBustedAsString() throws Exception {
        assertOutcomeEnumAndString(GameOutcome.PLAYER_BUSTED, "Player Busted");
    }

    @Test
    public void dealerBustedAsString() throws Exception {
        assertOutcomeEnumAndString(GameOutcome.DEALER_BUSTED, "Dealer Busted");
    }

    @Test
    void playerLosesAsString() {
        assertOutcomeEnumAndString(GameOutcome.PLAYER_LOSES, "Player Loses");
    }

    @Test
    public void playerWinsBlackjack() throws Exception {
        assertOutcomeEnumAndString(GameOutcome.BLACKJACK, "Player Wins Blackjack");
    }

    @Test
    public void playerBeatsDealer() throws Exception {
        assertOutcomeEnumAndString(GameOutcome.PLAYER_BEATS_DEALER, "Player Beats Dealer");
    }

    @Test
    public void playerPushesDealer() throws Exception {
        assertOutcomeEnumAndString(GameOutcome.PLAYER_PUSHES_DEALER, "Player Pushes Dealer");
    }

    private void assertOutcomeEnumAndString(GameOutcome gameOutcome, String expected) {
        OutcomeDto outcomeDto = new OutcomeDto(gameOutcome);

        assertThat(outcomeDto.asString()).isEqualTo(expected);
    }
}
