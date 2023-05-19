package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void givenPlayerBustsWhenPlayerHitsThenThrowsGameAlreadyOverException() throws Exception {
        Deck playerBustsDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE,
                                            Rank.TEN);
        Game game = createGameAndInitialDeal(1, playerBustsDeck);
        game.playerHits();

        assertThatThrownBy(game::playerHits)
                .isInstanceOf(GameAlreadyOver.class);
    }

    @Test
    void givenGameWithTwoPlayersWhenInitialDealThenEachPlayerHasTwoCards() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = createGameAndInitialDeal(2, noBlackjackDeck);
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
        Game game = new Game(PlayerCount.of(1), new Shoe(List.of(noBlackjackDeck)));

        assertThat(game.currentPlayerId())
                .isEqualTo(0);
    }

    @Test
    public void givenMultiplePlayersEachPlayerGetsUniqueIdAssigned() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = createGameAndInitialDeal(2, noBlackjackDeck);
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
        Game game = createGameAndInitialDeal(2, noBlackjackDeck);

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    private Game createGameAndInitialDeal(int playerCount, Deck deck) {
        Game game = new Game(PlayerCount.of(playerCount), new Shoe(List.of(deck)));
        List<Bet> bets = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            bets.add(Bet.of(42));
        }
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }

    @Test
    public void givenSinglePlayerGoesBustThenPlayerResultHasBustedOutcome() {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        Game game = createGameAndInitialDeal(1, deck);
        game.playerHits();

        List<PlayerResult> players = game.playerResults();

        assertThat(players)
                .hasSize(1);
        assertThat(players.get(0).outcome())
                .isEqualTo(PlayerOutcome.PLAYER_BUSTED);
    }

    @Test
    public void givenMultiPlayerGameThenPlayerResultsHasOutcomeForEachPlayer() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory
                .twoPlayersAllDealtBlackjackDealerCouldHit();
        Game game = new Game(PlayerCount.of(2), new Shoe(List.of(deck)));
        game.placeBets(List.of(Bet.of(11), Bet.of(22)));
        game.initialDeal();

        List<PlayerResult> playerResults = game.playerResults();

        assertThat(playerResults)
                .hasSize(2);
        assertThat(playerResults.get(0).outcome())
                .isEqualTo(PlayerOutcome.BLACKJACK);
        assertThat(playerResults.get(1).outcome())
                .isEqualTo(PlayerOutcome.BLACKJACK);
        assertThat(playerResults.get(0).bet())
                .isEqualTo(Bet.of(11));
        assertThat(playerResults.get(1).bet())
                .isEqualTo(Bet.of(22));

    }

    @Test
    public void givenTwoPlayersPlayerGoesBustNextPlayerCanStand() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.NINE, Rank.THREE, Rank.ACE,
                                            Rank.THREE, Rank.EIGHT, Rank.FOUR,
                                            Rank.KING, Rank.SEVEN, Rank.SIX);
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(noBlackjackDeck);
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
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(deck);
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
        Game game = GameFactory.createOnePlayerGame();

        assertThatThrownBy(game::playerStands)
                .isInstanceOf(CardsNotDealt.class);
    }

    @Test
    void cardsNotDealtPlayerHitsThrowsException() {
        Game game = GameFactory.createOnePlayerGame();

        assertThatThrownBy(game::playerHits)
                .isInstanceOf(CardsNotDealt.class);
    }

    @Test
    void betsOfNewGameAreEmpty() {
        Game game = GameFactory.createOnePlayerGame();

        assertThat(game.currentBets())
                .isEmpty();
    }

    @Test
    void placeBetsAfterInitialDealThrowsException() {
        Game game = GameFactory.createOnePlayerGamePlaceBets();
        game.initialDeal();

        assertThatThrownBy(() -> game.placeBets(List.of(Bet.of(1))))
                .isInstanceOf(CannotPlaceBetsAfterInitialDeal.class);
    }

    @Test
    void gameRemembersBetsPlaced() throws Exception {
        Game game = GameFactory.createOnePlayerGame();

        game.placeBets(List.of(Bet.of(6)));

        assertThat(game.currentBets())
                .containsExactly(Bet.of(6));
    }

    @Test
    void requireNumberOfBetsMatchPlayerCount() {
        Deck deck = StubDeckBuilder.buildTwoPlayerFixedDeck();
        Game game = new Game(PlayerCount.of(2), new Shoe(List.of(deck)));

        assertThatThrownBy(() -> game.placeBets(List.of(Bet.of(6), Bet.of(7), Bet.of(8))))
                .isInstanceOf(BetsNotMatchingPlayerCount.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 101})
    public void doNotAllowInvalidBetAmounts(int invalidBetAmount) {
        Game game = GameFactory.createOnePlayerGame();

        assertThatThrownBy(() -> game.placeBets(List.of(Bet.of(invalidBetAmount))))
                .isExactlyInstanceOf(InvalidBetAmount.class);
    }

    @Test
    public void invalidPlacedBetCallDoesNotStoreBets() {
        Game game = GameFactory.createOnePlayerGame();
        List<Bet> bets = List.of(Bet.of(11), Bet.of(22));

        try {
            game.placeBets(bets);
        } catch (Exception ex) {
            // ignore
        }

        assertThat(game.currentBets()).isEmpty();
    }

    @Test
    public void doNotAllowBetsToBePlacedTwice() {
        Game game = GameFactory.createOnePlayerGamePlaceBets();

        assertThatThrownBy(() -> game.placeBets(List.of(Bet.of(11))))
                .isExactlyInstanceOf(BetsAlreadyPlaced.class);
    }

    @Test
    public void initialDealWhenNoBetsPlacedThrowsException() throws Exception {
        Game game = GameFactory.createOnePlayerGame();

        assertThatThrownBy(game::initialDeal)
                .isExactlyInstanceOf(BetsNotPlaced.class);
    }

}