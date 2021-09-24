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
        Deck stubDeck = new StubDeck(Rank.TEN, Rank.QUEEN, Rank.JACK);
        player.drawFrom(stubDeck);
        player.drawFrom(stubDeck);

        player.hit(stubDeck);

        assertThat(player.reasonDone())
                .isEqualTo("Player busted");
    }

    @Test
    void playerDealtBlackjackReasonIsPlayerHasBlackjack() {
        Player player = new Player();
        Deck stubDeck = new StubDeck(Rank.TEN, Rank.ACE);

        player.drawFrom(stubDeck);
        player.drawFrom(stubDeck);

        assertThat(player.reasonDone())
                .isEqualTo("Player has blackjack");
    }

    @Test
    void playerNotDoneReasonDoneThrowsException() {
        Player player = new Player();

        assertThatThrownBy(() -> player.reasonDone())
                .isInstanceOf(IllegalStateException.class);
    }
}