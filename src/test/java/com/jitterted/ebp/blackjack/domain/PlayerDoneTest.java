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
                .isEqualTo(PlayerReasonDone.PLAYER_STANDS);
    }

    @Test
    public void playerHitsAndGoesBustReasonIsPlayerBusted() throws Exception {
        Player player = new Player();
        Deck stubDeck = new StubDeck(Rank.TEN, Rank.QUEEN, Rank.JACK);
        Shoe shoe = new Shoe(DeckFactory.createForTest(stubDeck).decks());
        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);

        player.hit(shoe);

        assertThat(player.reasonDone())
                .isEqualTo(PlayerReasonDone.PLAYER_BUSTED);
    }

    @Test
    void playerDealtBlackjackReasonIsPlayerHasBlackjack() {
        Player player = new Player();
        Deck stubDeck = new StubDeck(Rank.TEN, Rank.ACE);
        Shoe shoe = new Shoe(DeckFactory.createForTest(stubDeck).decks());

        player.initialDrawFrom(shoe);
        player.initialDrawFrom(shoe);

        assertThat(player.reasonDone())
                .isEqualTo(PlayerReasonDone.PLAYER_HAS_BLACKJACK);
    }

    @Test
    void playerNotDoneReasonDoneThrowsException() {
        Player player = new Player();

        assertThatThrownBy(player::reasonDone)
                .isInstanceOf(IllegalStateException.class);
    }
}