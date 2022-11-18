package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MultiPlayerGameTurnTest {

    @Test
    public void skipPastPlayerInMiddleWhoHasBlackjack() throws Exception {
        StubDeck deck = new StubDeck(Rank.KING, Rank.JACK, Rank.TEN, Rank.KING,
                                     Rank.NINE, Rank.ACE, Rank.TWO, Rank.FIVE);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(3, new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(2);
    }

    @Test
    void skipPastFirstPlayerWhoHasBlackjack() {
        StubDeck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING,
                                     Rank.ACE, Rank.TWO, Rank.FIVE);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(2, new Shoe(deckFactory));

        game.initialDeal();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void skipPastTwoPlayersHavingBlackjack() throws Exception {
        StubDeck deck = new StubDeck(Rank.KING, Rank.JACK, Rank.QUEEN, Rank.TEN, Rank.KING,
                                     Rank.NINE, Rank.ACE, Rank.ACE, Rank.TWO, Rank.FIVE);
        final DeckFactory deckFactory = new DeckFactory(deck);
        Game game = new Game(4, new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(3);
    }
}