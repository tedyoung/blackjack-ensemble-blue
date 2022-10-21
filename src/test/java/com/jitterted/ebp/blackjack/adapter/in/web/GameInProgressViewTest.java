package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.DeckFactory;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.StubDeckBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameInProgressViewTest {

    @Test
    public void twoPlayerGameHasNoEventsOnInitialDeal() {
        StubDeck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING,
                                     Rank.SIX, Rank.TWO, Rank.FIVE);
      final DeckFactory deckFactory = new DeckFactory(deck);
      Game game = new Game(2, new Shoe(deckFactory));
        game.initialDeal();

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getPlayerEvents())
                .isEmpty();
    }

    @Test
    public void twoPlayerGameHasEventAfterFirstPlayerStands() {
        StubDeck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING,
                                     Rank.SIX, Rank.TWO, Rank.FIVE);
      final DeckFactory deckFactory = new DeckFactory(deck);
      Game game = new Game(2, new Shoe(deckFactory));
        game.initialDeal();
        game.playerStands();

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getPlayerEvents())
                .hasSize(1);
        assertThat(gameInProgressView.getPlayerEvents().get(0))
                .isEqualTo("0: Player stands");
    }

    @Test
    public void threePlayerGameHasTwoEventsAfterFirstPlayerHasBlackjackAndSecondPlayerStands() {
        StubDeck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING, Rank.QUEEN,
                                     Rank.ACE, Rank.TWO, Rank.FIVE, Rank.EIGHT);
      final DeckFactory deckFactory = new DeckFactory(deck);
      Game game = new Game(3, new Shoe(deckFactory));
        game.initialDeal();
        game.playerStands();

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getPlayerEvents())
                .hasSize(2);
        assertThat(gameInProgressView.getPlayerEvents().get(0))
                .isEqualTo("0: Player has blackjack");
        assertThat(gameInProgressView.getPlayerEvents().get(1))
                .isEqualTo("1: Player stands");
    }

    @Test
    public void gameInitialDealThenDealerSecondCardFaceDown() {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                .buildWithDealerRanks(Rank.SEVEN, Rank.QUEEN);

      final DeckFactory deckFactory = new DeckFactory(deck);
      Game game = new Game(1, new Shoe(deckFactory));
        game.initialDeal();

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getDealerCards().get(0))
                .isEqualTo("7♥");
        assertThat(gameInProgressView.getDealerCards().get(1))
                .isEqualTo("XX");
    }
}