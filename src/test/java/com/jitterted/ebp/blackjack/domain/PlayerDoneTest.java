package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerDoneTest {

    @Test
    public void newPlayerIsNotDone() throws Exception {
        Player player = new Player();

        assertThat(player.isDone())
                .isFalse();
    }
    
    @Test
    public void playerStandsReasonIsPlayerStands() throws Exception {
        Player player = new Player();

        player.stand();

        assertThat(player.reasonDone())
                .isEqualTo("Player stands");
    }

    @Test
    public void playerHitsAndGoesBustReasonIsPlayerBusted() throws Exception {
        Player player = new Player();
        Deck stubDeck = new StubDeck(

        player.hit(

        assertThat(player.reasonDone())
                .isEqualTo("Player stands");
    }

}