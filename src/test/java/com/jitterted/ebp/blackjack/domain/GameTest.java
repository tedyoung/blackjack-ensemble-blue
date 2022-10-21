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
      final DeckFactory deckFactory = new DeckFactory(playerBustsDeck);
      Game game = new Game(1, new Shoe(deckFactory));
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
      final DeckFactory deckFactory = new DeckFactory(noBlackjackDeck);
      Game game = new Game(2, new Shoe(deckFactory));

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
      final DeckFactory deckFactory = new DeckFactory(noBlackjackDeck);
      Game game = new Game(1, new Shoe(deckFactory));

        assertThat(game.currentPlayerId())
                .isEqualTo(0);
    }

    @Test
    public void givenMultiplePlayersEachPlayerGetsUniqueIdAssigned() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
      final DeckFactory deckFactory = new DeckFactory(noBlackjackDeck);
      Game game = new Game(2, new Shoe(deckFactory));
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
      final DeckFactory deckFactory = new DeckFactory(noBlackjackDeck);
      Game game = new Game(2, new Shoe(deckFactory));
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void givenSinglePlayerGoesBustThenPlayerResultHasBustedOutcome() {
      final DeckFactory deckFactory = new DeckFactory(SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit());
      Game game = new Game(1, new Shoe(deckFactory));
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
      final DeckFactory deckFactory = new DeckFactory(MultiPlayerStubDeckFactory.twoPlayersAllDealtBlackjackDealerCouldHit());
      Game game = new Game(2, new Shoe(deckFactory));
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
      final DeckFactory deckFactory = new DeckFactory(noBlackjackDeck);
      Game game = new Game(2, new Shoe(deckFactory));
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
      final DeckFactory deckFactory = new DeckFactory(deck);
      Game game = new Game(2, new Shoe(deckFactory));

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

}