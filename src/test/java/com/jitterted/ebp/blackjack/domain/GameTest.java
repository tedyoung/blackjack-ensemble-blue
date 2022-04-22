package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

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

        assertThat(game.isGameOver())
                .isFalse();
        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
        assertThat(game.isGameOver())
                .isTrue();

    }

    @Test
    public void gameWhereDealerSecondCardIsFaceDownAfterInitialDeal() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);

        game.initialDeal();

        assertThat(game.dealerHand().cards().get(0).isFaceDown())
                .isFalse();
        assertThat(game.dealerHand().cards().get(1).isFaceDown())
                .isTrue();
    }

    @Test
    public void playerCardsAreAlwaysFaceUpAfterInitialDeal() {
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);

        game.initialDeal();

        assertThat(game.currentPlayerCards().get(0).isFaceDown())
                .isFalse();
        assertThat(game.currentPlayerCards().get(1).isFaceDown())
                .isFalse();
    }

    @Test
    public void whenPlayerStandsDealerSecondCardIsFaceUpAfterDealerTurn() {
        Game game = new Game(StubDeckBuilder.playerCountOf(1)
                                            .addPlayerWithRanks(Rank.TEN, Rank.JACK)
                                            .buildWithDealerDoesNotDrawCards(), 1);
        game.initialDeal();

        game.playerStands();

        assertThat(game.dealerHand().cards().get(0).isFaceDown())
                .isFalse();
        assertThat(game.dealerHand().cards().get(1).isFaceDown())
                .isFalse();
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
                .containsExactly("Dealer dealt blackjack");
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
                .containsExactly("Dealer dealt blackjack", "Dealer dealt blackjack");
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
                .containsExactly("Dealer dealt blackjack");
    }
}
