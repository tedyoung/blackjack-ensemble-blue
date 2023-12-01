package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    @Test
    void givenGameAssignPlayerAnIdAndCheckIt() {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = new Game(List.of(PlayerId.of(73)), new Shoe(List.of(noBlackjackDeck)));

        assertThat(game.currentPlayerId())
                .isEqualTo(PlayerId.of(73));
    }

    @Test
    void givenPlayerBustsWhenPlayerHitsThenThrowsGameAlreadyOverException() throws Exception {
        Deck playerBustsDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE,
                                            Rank.TEN);
        Game game = GameBuilder.createOnePlayerGamePlaceBetsInitialDeal(playerBustsDeck);
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
        Game game = GameBuilder.playerCountOf(3)
                               .deck(playersAndDealerStandsDeck)
                               .withDefaultPlayers()
                               .placeDefaultBets()
                               .initialDeal()
                               .build();
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
    void givenMultiplePlayersEachPlayerGetsUniqueIdAssigned() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(noBlackjackDeck,
                                                                        PlayerId.of(64),
                                                                        PlayerId.of(75)
        );
        game.playerStands();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::playerId)
                .containsExactly(PlayerId.of(64), PlayerId.of(75));
    }

    @Test
    void givenTwoPlayersWhenFirstPlayerStandsThenSecondPlayerIsCurrent() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        PlayerId firstPlayer = PlayerId.of(87);
        PlayerId secondPlayer = PlayerId.of(83);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(
                noBlackjackDeck, firstPlayer, secondPlayer);

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(secondPlayer);
    }

    @Test
    void givenSinglePlayerGoesBustThenPlayerResultHasBustedOutcome() {
        Deck deck = SinglePlayerStubDeckFactory.createPlayerHitsGoesBustDeckAndDealerCanNotHit();
        Game game = GameBuilder.createOnePlayerGamePlaceBetsInitialDeal(deck);
        game.playerHits();

        List<PlayerResult> players = game.playerResults();

        assertThat(players)
                .hasSize(1);
        assertThat(players.get(0).outcome())
                .isEqualTo(PlayerOutcome.PLAYER_BUSTED);
    }

    @Test
    void givenMultiPlayerGameThenPlayerResultsHasOutcomeForEachPlayer() throws Exception {
        Deck deck = MultiPlayerStubDeckFactory
                .twoPlayersAllDealtBlackjackDealerCouldHit();
        Game game = GameBuilder.playerCountOf(2)
                               .deck(deck)
                               .addPlayer(PlayerId.of(132), Bet.of(11))
                               .addPlayer(PlayerId.of(141), Bet.of(22))
                               .initialDeal()
                               .build();

        List<PlayerResult> playerResults = game.playerResults();

        assertThat(playerResults)
                .hasSize(2);
        assertThat(playerResults.get(0).playerId())
                .isEqualTo(PlayerId.of(132));
        assertThat(playerResults.get(1).playerId())
                .isEqualTo(PlayerId.of(141));
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
    void givenTwoPlayersFirstPlayerGoesBustThenSecondPlayerCanStand() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.NINE, Rank.THREE, Rank.ACE,
                                            Rank.THREE, Rank.EIGHT, Rank.FOUR,
                                            Rank.KING, Rank.SEVEN, Rank.SIX);
        PlayerId firstPlayer = PlayerId.of(35);
        PlayerId secondPlayer = PlayerId.of(53);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(
                noBlackjackDeck, firstPlayer, secondPlayer);
        game.playerHits();

        assertThatNoException()
                .isThrownBy(game::playerStands);
    }

    @Test
    void firstPlayerBustedHasHigherPriorityThanPushAndDealerBusted() throws Exception {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerWithRanks(Rank.JACK, Rank.THREE, Rank.TEN)
                                       .addPlayerWithRanks(Rank.EIGHT, Rank.TEN)
                                       .buildWithDealerRanks(Rank.SEVEN, Rank.SEVEN, Rank.NINE);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck);
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
        Game game = GameBuilder.createOnePlayerGame();

        assertThatThrownBy(game::playerStands)
                .isInstanceOf(CardsNotDealt.class);
    }

    @Test
    void cardsNotDealtPlayerHitsThrowsException() {
        Game game = GameBuilder.createOnePlayerGame();

        assertThatThrownBy(game::playerHits)
                .isInstanceOf(CardsNotDealt.class);
    }

    @Test
    void betsOfNewGameAreEmpty() {
        Game game = GameBuilder.createOnePlayerGame();

        assertThat(game.currentBets())
                .isEmpty();
    }

    @Test
    void placeBetsAfterInitialDealThrowsException() {
        PlayerId playerId = PlayerId.of(66);
        Game game = GameBuilder.createOnePlayerGamePlaceBets(playerId);
        game.initialDeal();

        Bet bet = Bet.of(1);
        assertThatThrownBy(() -> game.placePlayerBets(List.of(new PlayerBet(playerId, bet))))
                .isInstanceOf(CannotPlaceBetsAfterInitialDeal.class);
    }

    @Test
    void gameRemembersBetsPlacedByPlayerId() {
        PlayerId playerIdOne = PlayerId.of(13);
        PlayerId playerIdTwo = PlayerId.of(56);
        Game game = GameBuilder.createTwoPlayerGame(playerIdOne, playerIdTwo);
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
        Game twoPlayerGame = GameBuilder.createTwoPlayerGame(PlayerId.of(77), PlayerId.of(88));

        List<PlayerBet> threeBets = createThreePlayerBets();
        assertThatThrownBy(() -> twoPlayerGame.placePlayerBets(threeBets))
                .isInstanceOf(BetsNotMatchingPlayerCount.class);
    }

    @Test
    void requirePlayersToExistInGameWhenPlacingBets() {
        PlayerId firstPlayer = PlayerId.of(77);
        PlayerId secondPlayer = PlayerId.of(88);
        Game twoPlayerGame = GameBuilder.createTwoPlayerGame(firstPlayer, secondPlayer);

        List<PlayerBet> playerBets = new ArrayList<>();
        playerBets.add(new PlayerBet(firstPlayer, Bet.of(22)));
        playerBets.add(new PlayerBet(PlayerId.of(999), Bet.of(22)));

        assertThatThrownBy(() -> twoPlayerGame.placePlayerBets(playerBets))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Player ID 999 was not found, player IDs in game: [77, 88]");
    }

    @Test
    void invalidPlacedBetCallDoesNotStoreBets() {
        Game onePlayerGame = GameBuilder.createOnePlayerGame();
        try {
            onePlayerGame.placePlayerBets(createThreePlayerBets());
        } catch (BetsNotMatchingPlayerCount ex) {
            // ignore
        }

        assertThat(onePlayerGame.currentBets())
                .isEmpty();
    }

    @Test
    void doNotAllowBetsToBePlacedTwice() {
        PlayerId playerId = PlayerId.of(66);
        Game game = GameBuilder.createOnePlayerGamePlaceBets(playerId);

        assertThatThrownBy(() -> game.placePlayerBets(List.of(new PlayerBet(playerId, Bet.of(24)))))
                .isExactlyInstanceOf(BetsAlreadyPlaced.class);
    }

    @Test
    void initialDealWhenNoBetsPlacedThrowsException() throws Exception {
        Game game = GameBuilder.createOnePlayerGame();

        assertThatThrownBy(game::initialDeal)
                .isExactlyInstanceOf(BetsNotPlaced.class);
    }

    @Test
    void exposesPlayerIds() {
        Game game = GameBuilder.createTwoPlayerGame(PlayerId.of(77), PlayerId.of(88));

        assertThat(game.playerIds())
                .containsExactly(PlayerId.of(77), PlayerId.of(88));
    }

    private static List<PlayerBet> createThreePlayerBets() {
        return List.of(
                new PlayerBet(PlayerId.of(10), Bet.of(15)),
                new PlayerBet(PlayerId.of(20), Bet.of(25)),
                new PlayerBet(PlayerId.of(30), Bet.of(35)));
    }

}
