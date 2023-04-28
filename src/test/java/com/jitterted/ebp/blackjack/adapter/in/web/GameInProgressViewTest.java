package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameFactory;
import com.jitterted.ebp.blackjack.domain.PlayerCount;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.StubDeckBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameInProgressViewTest {

    @Test
    public void twoPlayerGameHasNoEventsOnInitialDeal() {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(deck);

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getPlayerEvents())
                .isEmpty();
    }

    @Test
    public void twoPlayerGameHasEventAfterFirstPlayerStands() {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                           .addPlayerNotDealtBlackjack()
                                           .addPlayerNotDealtBlackjack()
                                           .buildWithDealerRanks(Rank.KING, Rank.FIVE);
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(deck);
        game.playerStands();

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getPlayerEvents())
                .hasSize(1);
        assertThat(gameInProgressView.getPlayerEvents().get(0))
                .isEqualTo("0: Player stands");
    }

    @Test
    public void threePlayerGameHasTwoEventsAfterFirstPlayerHasBlackjackAndSecondPlayerStands() {
        Deck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING, Rank.QUEEN,
                                 Rank.ACE, Rank.TWO, Rank.FIVE, Rank.EIGHT);
        Game game = GameFactory.createGamePlaceBetsInitialDeal(3, deck);
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
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .buildWithDealerRanks(Rank.SEVEN, Rank.QUEEN);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(deck);

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getDealerCards().get(0))
                .isEqualTo("7♥");
        assertThat(gameInProgressView.getDealerCards().get(1))
                .isEqualTo("XX");
    }
}