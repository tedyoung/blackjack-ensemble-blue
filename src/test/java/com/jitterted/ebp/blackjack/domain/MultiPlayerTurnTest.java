package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MultiPlayerTurnTest {

    @Test
    public void skipPastSinglePlayerWhoHasBlackjack() throws Exception {
        StubDeck deck = new StubDeck(Rank.KING, Rank.JACK, Rank.TEN, Rank.KING,
                                     Rank.NINE, Rank.ACE,  Rank.TWO, Rank.FIVE);
        Game game = new Game(deck, 3);
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(2);
    }

    @Test
    public void skipPastTwoPlayersHavingBlackjack() throws Exception {
        //                             0           1            2          3
        StubDeck deck = new StubDeck(Rank.KING, Rank.JACK, Rank.QUEEN, Rank.TEN, Rank.KING,
                                     Rank.NINE, Rank.ACE,  Rank.ACE,   Rank.TWO, Rank.FIVE);
        Game game = new Game(deck, 4);
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(3);
    }
}