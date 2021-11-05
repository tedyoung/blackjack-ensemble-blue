package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MultiPlayerTurnTest {

    @Test
    public void multiPlayerGameSkipPastPlayerWhoHasBlackjack() throws Exception {
        StubDeck deck = new StubDeck(Rank.KING, Rank.JACK, Rank.TEN, Rank.KING,
                                     Rank.NINE, Rank.ACE,  Rank.TWO, Rank.FIVE);
        Game game = new Game(deck, 3);
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(2);
    }

}