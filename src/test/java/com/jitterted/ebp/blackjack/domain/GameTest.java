package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void givenPlayerBustsWhenPlayerHitsThenThrowsException() throws Exception {
        Deck playerBustsDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE,
                                            Rank.TEN);
        Game game = new Game(playerBustsDeck, 1);
        game.initialDeal();
        game.playerHits();

        assertThatThrownBy(() -> {
            game.playerHits();
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void givenGameWithTwoPlayersWhenInitialDealThenEachPlayerHasTwoCards() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 2);

        game.initialDeal();
        game.playerStands();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::cards)
                .extracting(List::size)
                .containsExactly(2, 2);
    }

    @Test
    void givenGameAssignPlayerAnIdAndCheckIt() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 1);

        assertThat(game.currentPlayerId())
                .isEqualTo(0);
    }

    @Test
    public void givenMultiplePlayersEachPlayerGetsUniqueIdAssigned() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 2);
        game.initialDeal();
        game.playerStands();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::id)
                .containsExactly(0, 1);
    }

    @Test
    public void givenMultiplePlayersPlayerStandsWhenNextPlayerCommandThenSecondPlayerIsCurrent() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(noBlackjackDeck, 2);
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void givenSinglePlayerGoesBustThenPlayerResultHasBustedOutcome() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit(), 1);
        game.initialDeal();
        game.playerHits();

        List<PlayerResult> players = game.playerResults();

        assertThat(players)
                .hasSize(1);
        assertThat(players.get(0).outcome())
                .isEqualTo(PlayerOutcome.PLAYER_BUSTED);
    }

    @Test
    public void givenMultiPlayerGameThenPlayerResultsHasOutcomeForEachPlayer() throws Exception {
        Game game = new Game(MultiPlayerStubDeckFactory.twoPlayersAllDealtBlackjackDealerCouldHit(), 2);
        game.initialDeal();

        List<PlayerResult> players = game.playerResults();

        assertThat(players)
                .hasSize(2);
        assertThat(players.get(0).outcome())
                .isEqualTo(PlayerOutcome.BLACKJACK);
        assertThat(players.get(1).outcome())
                .isEqualTo(PlayerOutcome.BLACKJACK);
    }

    @Test
    public void givenTwoPlayersPlayerGoesBustNextPlayerCanStand() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.NINE, Rank.THREE, Rank.ACE,
                                            Rank.THREE, Rank.EIGHT, Rank.FOUR,
                                            Rank.KING, Rank.SEVEN, Rank.SIX);
        Game game = new Game(noBlackjackDeck, 2);
        game.initialDeal();
        game.playerHits();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void firstPlayerBustedHasHigherPriorityThanPushAndDealerBusted() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerWithRanks(Rank.JACK, Rank.THREE, Rank.TEN)
                                       .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.NINE);
        Game game = new Game(deck, 2);

        game.initialDeal();
        game.playerHits();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::outcome)
                .containsExactly(PlayerOutcome.PLAYER_BUSTED, PlayerOutcome.DEALER_BUSTED);
        assertThat(game.events())
                .extracting(PlayerDoneEvent::reasonDone)
                .containsExactly(PlayerReasonDone.PLAYER_BUSTED, PlayerReasonDone.PLAYER_STANDS);
    }

    @Test
    public void gameNeverRunsOutOfCards() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(1)
                                       .addPlayerDealtBlackjack()
                                       .buildWithDealerDoesNotDrawCards();
        Game game = new Game(deck, 1);
        game.initialDeal();

        Game nextGame = new Game(deck, 1);
        nextGame.initialDeal();

        assertThat(nextGame.currentPlayerCards())
                .hasSize(2);
    }
}