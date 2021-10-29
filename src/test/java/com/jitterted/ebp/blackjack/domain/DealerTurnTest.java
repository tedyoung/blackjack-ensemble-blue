package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DealerTurnTest {

    @Test
    public void singlePlayerDealtBlackjackDealerDoesNotTakeTheirTurn() throws Exception {
        Deck deck = SinglePlayerStubDeckFactory.Nooooooooooo
        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }

    @Test
    void givenMultiplePlayersFirstPlayerStandsThenDealerDoesNotTakeTheirTurn() {
        Deck deck = new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN,
                                 Rank.EIGHT, Rank.QUEEN, Rank.TWO, Rank.NINE);
        Game game = new Game(deck, 2);
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }
}