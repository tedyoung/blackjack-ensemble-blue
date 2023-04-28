package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MultiPlayerGameTurnTest {

    @Test
    public void skipPastPlayerInMiddleWhoHasBlackjack() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(3)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameFactory.createGamePlaceBetsInitialDeal(deck, 3);

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(2);
    }

    @Test
    void skipPastFirstPlayerWhoHasBlackjack() {
        StubDeck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING,
                                     Rank.ACE, Rank.TWO, Rank.FIVE);
        Game game = GameFactory.createTwoPlayerGamePlaceBets(deck);

        game.initialDeal();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void skipPastTwoPlayersHavingBlackjack() throws Exception {
        StubDeck deck = new StubDeck(Rank.KING, Rank.JACK, Rank.QUEEN, Rank.TEN, Rank.KING,
                                     Rank.NINE, Rank.ACE, Rank.ACE, Rank.TWO, Rank.FIVE);
        final List<Deck> deckFactory = List.of(deck);
        Game game = new Game(PlayerCount.of(4), new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(3);
    }
}