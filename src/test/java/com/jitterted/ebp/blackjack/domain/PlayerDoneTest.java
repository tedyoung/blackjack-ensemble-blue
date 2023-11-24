package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerDoneTest {

    @Test
    public void newPlayerIsNotDone() throws Exception {
        PlayerInGame player = new PlayerInGame(PlayerId.irrelevantPlayerId());

        assertThat(player.isDone())
                .isFalse();
    }

    @Test
    public void playerStandsReasonIsPlayerStands() throws Exception {
        PlayerInGame player = new PlayerInGame(PlayerId.irrelevantPlayerId());

        player.stand();

        assertThat(player.reasonDone())
                .isEqualTo(PlayerReasonDone.PLAYER_STANDS);
    }

    @Test
    public void playerHitsAndGoesBustReasonIsPlayerBusted() throws Exception {
        PlayerInGame player = new PlayerInGame(PlayerId.irrelevantPlayerId());
        Deck stubDeck = new StubDeck(Rank.TEN, Rank.QUEEN, Rank.JACK);
        Shoe shoe = new Shoe(List.of(stubDeck));
        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);

        player.hit(shoe);

        assertThat(player.reasonDone())
                .isEqualTo(PlayerReasonDone.PLAYER_BUSTED);
    }

    @Test
    void playerDealtBlackjackReasonIsPlayerHasBlackjack() {
        PlayerInGame player = new PlayerInGame(PlayerId.irrelevantPlayerId());
        Deck stubDeck = new StubDeck(Rank.TEN, Rank.ACE);
        Shoe shoe = new Shoe(List.of(stubDeck));

        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);

        assertThat(player.reasonDone())
                .isEqualTo(PlayerReasonDone.PLAYER_HAS_BLACKJACK);
    }

    @Test
    void playerNotDoneReasonDoneThrowsException() {
        PlayerInGame player = new PlayerInGame(PlayerId.irrelevantPlayerId());

        assertThatThrownBy(player::reasonDone)
                .isInstanceOf(IllegalStateException.class);
    }
}