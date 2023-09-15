package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameBuilder;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.StubDeckBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameInProgressViewTest {

    @Test
    public void twoPlayerGameHasNoEventsOnInitialDeal() {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck);

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
        PlayerId firstPlayer = new PlayerId(157);
        PlayerId secondPlayer = new PlayerId(179);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck, firstPlayer, secondPlayer);
        game.playerStands();

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getPlayerEvents())
                .hasSize(1);
        assertThat(gameInProgressView.getPlayerEvents().get(0))
                .isEqualTo("157: Player stands");
    }

    @Test
    public void threePlayerGameHasTwoEventsAfterFirstPlayerHasBlackjackAndSecondPlayerStands() {
        Deck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING, Rank.QUEEN,
                                 Rank.ACE, Rank.TWO, Rank.FIVE, Rank.EIGHT);
        Game game = GameBuilder.playerCountOf(3)
                          .deck(deck)
                          .addPlayer(new PlayerId(23))
                          .addPlayer(new PlayerId(47))
                          .addPlayer(new PlayerId(73))
                          .placeDefaultBets()
                          .initialDeal()
                          .build();
        game.playerStands();

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getPlayerEvents())
                .hasSize(2);
        assertThat(gameInProgressView.getPlayerEvents().get(0))
                .isEqualTo("23: Player has blackjack");
        assertThat(gameInProgressView.getPlayerEvents().get(1))
                .isEqualTo("47: Player stands");
    }

    @Test
    public void gameInitialDealThenDealerSecondCardFaceDown() {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .buildWithDealerRanks(Rank.SEVEN, Rank.QUEEN);
        Game game = GameBuilder.createOnePlayerGamePlaceBetsInitialDeal(deck);

        GameInProgressView gameInProgressView = GameInProgressView.of(game);

        assertThat(gameInProgressView.getDealerCards().get(0))
                .isEqualTo("7♥");
        assertThat(gameInProgressView.getDealerCards().get(1))
                .isEqualTo("XX");
    }
}