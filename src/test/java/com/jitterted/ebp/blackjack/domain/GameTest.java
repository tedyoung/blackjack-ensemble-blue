package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void givenGameAssignPlayerAnIdAndCheckIt() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(new Shoe(List.of(noBlackjackDeck)), List.of(new PlayerId(73)));

        assertThat(game.currentPlayerId())
                .isEqualTo(73);
    }

    @Test
    void givenPlayerBustsWhenPlayerHitsThenThrowsGameAlreadyOverException() throws Exception {
        Deck playerBustsDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE,
                                            Rank.TEN);
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(playerBustsDeck);
        game.playerHits();

        assertThatThrownBy(game::playerHits)
                .isInstanceOf(GameAlreadyOver.class);

        // #AssertJ -- the opposite order:
//        assertThatExceptionOfType(GameAlreadyOver.class)
//                .isThrownBy(game::playerHits);
    }

    @Test
    void gameWithMultiplePlayersWhenAllStandThenEachPlayerHasTwoCards() {
        Deck playersAndDealerStandsDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT, Rank.TEN, Rank.KING,
                                                       Rank.NINE, Rank.SEVEN, Rank.THREE, Rank.TEN);
        Game game = GameFactory.createMultiPlayerGamePlaceBetsInitialDeal(3, playersAndDealerStandsDeck);
        game.playerStands();
        game.playerStands();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::cards)
                .extracting(List::size)
                .containsExactly(2, 2, 2);
        // #AssertJ: alternative
//        assertThat(game.playerResults())
//                .extracting(PlayerResult::cards)
//                .extracting(List::size)
//                .hasSize(3)
//                .containsOnly(2);
    }

    @Test
    public void givenMultiplePlayersEachPlayerGetsUniqueIdAssigned() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(noBlackjackDeck);
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
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(noBlackjackDeck);

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(1);
    }

    @Test
    public void givenSinglePlayerGoesBustThenPlayerResultHasBustedOutcome() {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(deck);
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
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(
                deck,
                Bet.of(11),
                Bet.of(22));

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
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        PlayerId playerId = new PlayerId(66);
        Game game = new Game(shoe, List.of(playerId));
        game.placePlayerBets(List.of(new PlayerBet(playerId, Bet.of(42))));
        game.initialDeal();

        Bet bet = Bet.of(1);
        assertThatThrownBy(() -> game.placePlayerBets(List.of(new PlayerBet(playerId, bet))))
                .isInstanceOf(CannotPlaceBetsAfterInitialDeal.class);
    }

    @Test
    void gameRemembersBetsPlacedByPlayerId() {
        Deck deck = StubDeckBuilder.buildTwoPlayerFixedDeck();
        PlayerId playerIdOne = new PlayerId(13);
        PlayerId playerIdTwo = new PlayerId(56);
        List<PlayerId> playerIds = List.of(playerIdOne, playerIdTwo);
        Game game = new Game(new Shoe(List.of(deck)), playerIds);
        List<PlayerBet> bets = List.of(
                new PlayerBet(playerIdOne, Bet.of(11)),
                new PlayerBet(playerIdTwo, Bet.of(22)));

        game.placePlayerBets(bets);

        assertThat(game.currentBets())
                .containsExactly(new PlayerBet(playerIdOne, Bet.of(11)),
                                 new PlayerBet(playerIdTwo, Bet.of(22)));
    }

    @Test
    void requireNumberOfBetsMatchPlayerCount() {
        Deck deck = StubDeckBuilder.buildTwoPlayerFixedDeck();
        Game twoPlayerGame = new Game(new Shoe(List.of(deck)), PlayerCount.of(2));

        List<PlayerBet> threeBets = GameFactory.createPlayerBets(new PlayerCount(3));
        assertThatThrownBy(() -> twoPlayerGame.placePlayerBets(threeBets))
                .isInstanceOf(BetsNotMatchingPlayerCount.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 101})
    public void doNotAllowInvalidBetAmounts(int invalidBetAmount) {
        Game game = GameFactory.createOnePlayerGame();

        assertThatThrownBy(() -> game.placeBets(List.of(Bet.of(invalidBetAmount))))
                .isExactlyInstanceOf(InvalidBetAmount.class)
                .hasMessage("Bet amount: %d is not within 1 to 100", invalidBetAmount);
    }

    @Test
    public void invalidPlacedBetCallDoesNotStoreBets() {
        Game onePlayerGame = GameFactory.createOnePlayerGame();
        try {
            onePlayerGame.placePlayerBets(GameFactory.createPlayerBets(new PlayerCount(3)));
        } catch (BetsNotMatchingPlayerCount ex) {
            // ignore
        }

        assertThat(onePlayerGame.currentBets())
                .isEmpty();
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
