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
        Game game = new Game(1, new Shoe(List.of(playerBustsDeck)));
        game.initialDeal();
        game.playerHits();

        assertThatThrownBy(game::playerHits)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void givenGameWithTwoPlayersWhenInitialDealThenEachPlayerHasTwoCards() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(2, new Shoe(List.of(noBlackjackDeck)));

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
        Game game = new Game(1, new Shoe(List.of(noBlackjackDeck)));

        assertThat(game.currentPlayerId())
                .isEqualTo(0);
    }

    @Test
    public void givenMultiplePlayersEachPlayerGetsUniqueIdAssigned() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(2, new Shoe(List.of(noBlackjackDeck)));
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
        Game game = new Game(2, new Shoe(List.of(noBlackjackDeck)));
        game.initialDeal();

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void givenSinglePlayerGoesBustThenPlayerResultHasBustedOutcome() {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        Game game = new Game(1, new Shoe(List.of(deck)));
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
        final List<Deck> deckFactory = List.of(MultiPlayerStubDeckFactory.twoPlayersAllDealtBlackjackDealerCouldHit());
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
        final List<Deck> deckFactory = List.of(noBlackjackDeck);
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
        final List<Deck> deckFactory = List.of(deck);
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

    @Test
    void cardsNotDealtPlayerStandsThrowsException() {
        Game game = createOnePlayerGame();

        assertThatThrownBy(game::playerStands)
                .isInstanceOf(CardsNotDealt.class);
    }

    @Test
    void cardsNotDealtPlayerHitsThrowsException() {
        Game game = createOnePlayerGame();

        assertThatThrownBy(game::playerHits)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void betsOfNewGameAreEmpty() {
        Game game = createOnePlayerGame();

        assertThat(game.currentBets())
                .isEmpty();
    }

    @Test
    void placeBetsAfterInitialDealThrowsException() {
        Game game = createOnePlayerGame();
        game.initialDeal();

        assertThatThrownBy(() -> game.placeBets(List.of(1)))
                .isInstanceOf(CannotPlaceBetsAfterInitialDeal.class);
    }

    @Test
    public void gameRemembersBetsPlaced() throws Exception {
        Game game = createOnePlayerGame();

        game.placeBets(List.of(6));

        assertThat(game.currentBets())
                .containsExactly(6);
    }

    private Game createOnePlayerGame() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        return new Game(1, new Shoe(List.of(deck)));
    }

}