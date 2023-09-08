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
        Game game = GameFactory.createMultiPlayerGamePlaceBetsInitialDeal(List.of(
                new PlayerId(31),
                new PlayerId(23),
                new PlayerId(67)
        ), deck
        );

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(67);
    }

    @Test
    void skipPastFirstPlayerWhoHasBlackjack() {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        PlayerId firstPlayerId = new PlayerId(44);
        PlayerId secondPlayerId = new PlayerId(17);
        Game game = GameBuilder.createTwoPlayerGamePlaceBets(deck, firstPlayerId, secondPlayerId);

        game.initialDeal();

        assertThat(game.currentPlayerId())
                .isEqualTo(secondPlayerId.id());
    }

    @Test
    public void skipPastTwoPlayersHavingBlackjack() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(4)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();

        Game game = GameFactory.createMultiPlayerGamePlaceBetsInitialDeal(List.of(
                new PlayerId(31),
                new PlayerId(23),
                new PlayerId(67),
                new PlayerId(89)
        ), deck
        );

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(89);
    }
}