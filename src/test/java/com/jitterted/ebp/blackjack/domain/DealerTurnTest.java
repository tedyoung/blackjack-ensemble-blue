package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DealerTurnTest {

    @Test
    public void singlePlayerDealtBlackjackDealerDoesNotTakeTheirTurn() throws Exception {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCouldHit();
        Game game = new Game(deck);

        game.initialDeal();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }

    @Test
    public void multiPlayerAllPlayersDealtBlackjackDealerDoesNotTakeTheirTurn() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersAllDealtBlackjackDealerCouldHit();
        Game game = new Game(deck, 2);

        game.initialDeal();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }
    
    @Test
    public void multiPlayerAllPlayersButOneDealtBlackjackDealerTakesTheirTurn() throws Exception {
        fail("start here");
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