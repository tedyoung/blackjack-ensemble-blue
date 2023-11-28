package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MultiPlayerGameTurnTest {

    @Test
    void skipPastPlayerInMiddleWhoHasBlackjack() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(3)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameBuilder.playerCountOf(3)
                               .deck(deck)
                               .addPlayer(PlayerId.of(31))
                               .addPlayer(PlayerId.of(47))
                               .addPlayer(PlayerId.of(67))
                               .placeDefaultBets()
                               .initialDeal()
                               .build();

        game.playerStands();

        assertThat(game.currentPlayerId().id())
                .isEqualTo(67);
    }

    @Test
    void skipPastFirstPlayerWhoHasBlackjack() {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        PlayerId firstPlayerId = PlayerId.of(44);
        PlayerId secondPlayerId = PlayerId.of(17);
        Game game = GameBuilder.createTwoPlayerGamePlaceBets(deck, firstPlayerId, secondPlayerId);

        game.initialDeal();

        assertThat(game.currentPlayerId())
                .isEqualTo(secondPlayerId);
    }

    @Test
    void skipPastTwoPlayersHavingBlackjack() throws Exception {
        Deck deck = StubDeckBuilder.playerCountOf(4)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        Game game = GameBuilder.playerCountOf(4)
                               .deck(deck)
                               .addPlayer(PlayerId.of(23))
                               .addPlayer(PlayerId.of(47))
                               .addPlayer(PlayerId.of(73))
                               .addPlayer(PlayerId.of(89))
                               .placeDefaultBets()
                               .initialDeal()
                               .build();

        game.playerStands();

        assertThat(game.currentPlayerId().id())
                .isEqualTo(89);
    }
}