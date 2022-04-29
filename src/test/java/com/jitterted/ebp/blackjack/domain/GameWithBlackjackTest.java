package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameWithBlackjackTest {

    @Test
    void givenPlayerDealtBlackjackWhenPlayerHitsThenThrowsException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        Game game = new Game(playerDrawsBlackjackDeck, 1);
        game.initialDeal();

        assertThatThrownBy(() -> {
            game.playerHits();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void givenPlayerDealtBlackjackWhenPlayerStandsThrowsException() throws Exception {
        Deck playerDrawsBlackjackDeck = new StubDeck(Rank.KING, Rank.TWO, Rank.ACE, Rank.EIGHT, Rank.TEN);
        Game game = new Game(playerDrawsBlackjackDeck, 1);
        game.initialDeal();

        assertThatThrownBy(() -> {
            game.playerStands();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void givenSinglePlayerDealtBlackjackThenResultHasBlackjackOutcome() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);
        game.initialDeal();

        List<PlayerResult> players = game.playerResults();

        assertThat(players)
                .hasSize(1);
        assertThat(players.get(0).outcome())
                .isEqualTo(PlayerOutcome.BLACKJACK);
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOver() throws Exception {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                            .buildWithDealerDealtBlackjack(), 1);

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.PLAYER_LOSES);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.DEALER_DEALT_BLACKJACK);
    }

    @Test
    public void whenDealerDealtBlackjackGameIsOverForTwoPlayers() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                       .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                       .buildWithDealerDealtBlackjack();
        Game game = new Game(deck, 2);

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.PLAYER_LOSES, PlayerOutcome.PLAYER_LOSES);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.DEALER_DEALT_BLACKJACK, PlayerReasonDone.DEALER_DEALT_BLACKJACK);
    }

    @Test
    public void bothDealerAndPlayerDealtBlackjackResultIsPushes() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerDealtBlackjack()
                                       .buildWithDealerDealtBlackjack();
        Game game = new Game(deck, 1);

        game.initialDeal();

        assertThat(game.isGameOver())
                .isTrue();
        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.PLAYER_PUSHES_DEALER);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.DEALER_DEALT_BLACKJACK);
    }

    @Test
    public void playerBlackjackHasHigherPriorityThanPush() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerDealtBlackjack()
                                       .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.SEVEN);
        Game game = new Game(deck, 2);

        game.initialDeal();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.BLACKJACK, PlayerOutcome.PLAYER_LOSES);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.PLAYER_HAS_BLACKJACK, PlayerReasonDone.PLAYER_STANDS);
    }

    @Test
    public void allPlayersDealtBlackjackDealerDoesNotHaveBlackjackDealerDoesNotTakeTurn() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerDealtBlackjack()
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.SEVEN);
        Game game = new Game(deck, 1);

        game.initialDeal();

        assertThat(game.dealerHand().cards())
                .hasSize(2);
    }
}