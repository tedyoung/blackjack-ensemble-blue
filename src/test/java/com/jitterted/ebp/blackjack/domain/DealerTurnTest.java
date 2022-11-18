package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DealerTurnTest {

    @Test
    public void multiPlayerAllPlayersDealtBlackjackDealerDoesNotTakeTheirTurn() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory.twoPlayersAllDealtBlackjackDealerCouldHit();
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(2, new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }

    @Test
    public void multiPlayerOneDealtBlackjackOneStandsDealerTakesTheirTurn() throws Exception {
        StubDeck deck = new StubDeck(Rank.KING, Rank.JACK, Rank.TEN,
                                     Rank.NINE, Rank.ACE, Rank.TWO,
                                     Rank.FIVE);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(2, new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards())
                .hasSize(3);
    }

    @Test
    public void multiPlayerOneDealtBlackjackOneBustedDealerDoesNotTakeTheirTurn() throws Exception {
        Deck deck = new StubDeck(Rank.ACE, Rank.TEN, Rank.TEN,
                                 Rank.JACK, Rank.FIVE, Rank.TWO,
                                 Rank.NINE);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(2, new Shoe(deckFactory));
        game.initialDeal();

        game.playerHits();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }

    @Test
    public void multiPlayerFirstPlayerStandsThenDealerDoesNotTakeTheirTurn() {
        Deck deck = new StubDeck(Rank.QUEEN, Rank.KING, Rank.TEN,
                                 Rank.EIGHT, Rank.QUEEN, Rank.TWO, Rank.NINE);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(2, new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }

    @Test
    public void singlePlayerDealtBlackjackDealerDoesNotTakeTheirTurn() throws Exception {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCouldHit();
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(1, new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }

    @Test
    public void singlePlayerStandsThenDealerTakesTheirTurn() throws Exception {
        Deck deck = new StubDeck(Rank.QUEEN, Rank.TEN,
                                 Rank.EIGHT, Rank.TWO, Rank.NINE);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(1, new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards())
                .hasSize(3);
    }

    @Test
    public void singlePlayerGoesBustDealerDoesNotTakeTheirTurn() {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCouldHit();
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(1, new Shoe(deckFactory));
        game.initialDeal();

        game.playerHits();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }
}