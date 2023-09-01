package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

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
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(noBlackjackDeck,
                                                                        new PlayerId(64),
                                                                        new PlayerId(75)
        );
        game.playerStands();
        game.playerStands();

        assertThat(game.playerResults())
                .extracting(PlayerResult::id)
                .containsExactly(64, 75);
    }

    @Test
    public void givenTwoPlayersWhenFirstPlayerStandsThenSecondPlayerIsCurrent() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                            Rank.TEN, Rank.FOUR,
                                            Rank.THREE, Rank.TEN);
        PlayerId firstPlayer = new PlayerId(87);
        PlayerId secondPlayer = new PlayerId(83);
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(
                noBlackjackDeck, firstPlayer, secondPlayer);

        game.playerStands();

        assertThat(game.currentPlayerId())
                .isEqualTo(secondPlayer.id());
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
                new PlayerBet(new PlayerId(132), Bet.of(11)),
                new PlayerBet(new PlayerId(141), Bet.of(22)));

        List<PlayerResult> playerResults = game.playerResults();

        assertThat(playerResults)
                .hasSize(2);
        assertThat(playerResults.get(0).id())
                .isEqualTo(132);
        assertThat(playerResults.get(1).id())
                .isEqualTo(141);
        assertThat(playerResults.get(0 ).outcome())
                .isEqualTo(PlayerOutcome.BLACKJACK);
        assertThat(playerResults.get(1).outcome())
                .isEqualTo(PlayerOutcome.BLACKJACK);
        assertThat(playerResults.get(0).bet())
                .isEqualTo(Bet.of(11));
        assertThat(playerResults.get(1).bet())
                .isEqualTo(Bet.of(22));

    }

    @Test
    public void givenTwoPlayersFirstPlayerGoesBustThenSecondPlayerCanStand() throws Exception {
        Deck noBlackjackDeck = new StubDeck(Rank.NINE, Rank.THREE, Rank.ACE,
                                            Rank.THREE, Rank.EIGHT, Rank.FOUR,
                                            Rank.KING, Rank.SEVEN, Rank.SIX);
        PlayerId firstPlayer = new PlayerId(35);
        PlayerId secondPlayer = new PlayerId(53);
        Game game = GameFactory.createTwoPlayerGamePlaceBetsInitialDeal(
                noBlackjackDeck, firstPlayer, secondPlayer);
        game.playerHits();

        assertThatNoException()
                .isThrownBy(game::playerStands);
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
        PlayerId playerId = new PlayerId(66);
        Game game = GameBuilder.createOnePlayerGamePlaceBets(playerId);
        game.initialDeal();

        Bet bet = Bet.of(1);
        assertThatThrownBy(() -> game.placePlayerBets(List.of(new PlayerBet(playerId, bet))))
                .isInstanceOf(CannotPlaceBetsAfterInitialDeal.class);
    }

    @Test
    void gameRemembersBetsPlacedByPlayerId() {
        PlayerId playerIdOne = new PlayerId(13);
        PlayerId playerIdTwo = new PlayerId(56);
        Game game = GameFactory.createTwoPlayerGame(playerIdOne, playerIdTwo);
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
        Game twoPlayerGame = GameFactory.createTwoPlayerGame(new PlayerId(77), new PlayerId(88));

        List<PlayerBet> threeBets = GameFactory.createPlayerBets(new PlayerCount(3));
        assertThatThrownBy(() -> twoPlayerGame.placePlayerBets(threeBets))
                .isInstanceOf(BetsNotMatchingPlayerCount.class);
    }

    @Test
    public void invalidPlacedBetCallDoesNotStoreBets() {
        Game onePlayerGame = GameBuilder.createOnePlayerGame();
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
        PlayerId playerId = new PlayerId(66);
        Game game = GameBuilder.createOnePlayerGamePlaceBets(playerId);

        assertThatThrownBy(() -> game.placePlayerBets(List.of(new PlayerBet(playerId, Bet.of(24)))))
                .isExactlyInstanceOf(BetsAlreadyPlaced.class);
    }

    @Test
    public void initialDealWhenNoBetsPlacedThrowsException() throws Exception {
        Game game = GameBuilder.createOnePlayerGame();

        assertThatThrownBy(game::initialDeal)
                .isExactlyInstanceOf(BetsNotPlaced.class);
    }

}
