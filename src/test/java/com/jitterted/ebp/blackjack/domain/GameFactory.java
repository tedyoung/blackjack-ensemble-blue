package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameFactory {
    public static Game createOnePlayerGame() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        List<PlayerId> playerIds = List.of(new PlayerId(54));
        return new Game(shoe, playerIds);
    }

    public static Game createOnePlayerGamePlaceBets(PlayerId playerId) {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        Game game = new Game(shoe, List.of(playerId));
        game.placePlayerBets(List.of(new PlayerBet(playerId, Bet.of(42))));
        return game;
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck) {
        Shoe shoe = new Shoe(List.of(deck));
        return createOnePlayerGamePlaceBets(shoe);
    }

    public static Game createOnePlayerGamePlaceBets(Shoe shoe) {
        return createOnePlayerGamePlaceBets(shoe, new PlayerId(42));
    }

    public static Game createOnePlayerGamePlaceBets(Shoe shoe, PlayerId playerId) {
        Game game = new Game(shoe, List.of(playerId));
        List<PlayerBet> bets = List.of(new PlayerBet(playerId, Bet.of(3)));
        game.placePlayerBets(bets);
        return game;
    }

    public static Game createOnePlayerGamePlaceBetsInitialDeal(Deck deck) {
        Game game = createOnePlayerGamePlaceBets(deck);
        game.initialDeal();
        return game;
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck, PlayerId playerId) {
        return createOnePlayerGamePlaceBets(new Shoe(List.of(deck)), playerId);
    }

    public static Game createTwoPlayerGame(PlayerId playerIdOne, PlayerId playerIdTwo) {
        Deck deck = StubDeckBuilder.buildTwoPlayerFixedDeck();
        List<PlayerId> playerIds = List.of(playerIdOne, playerIdTwo);
        return new Game(new Shoe(List.of(deck)), playerIds);
    }

    public static Game createTwoPlayerGamePlaceBets(Deck deck, PlayerId playerOne, PlayerId playerTwo) {
        List<PlayerId> playerIds = List.of(playerOne, playerTwo);
        Game game = new Game(new Shoe(List.of(deck)), playerIds);
        List<PlayerBet> bets = List.of(
                new PlayerBet(playerOne, new Bet(11)),
                new PlayerBet(playerTwo, new Bet(22)));
        game.placePlayerBets(bets);
        return game;
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal
            (Deck deck, PlayerId firstPlayer, PlayerId secondPlayer) {
        PlayerBet firstBet = new PlayerBet(firstPlayer, Bet.of(23));
        PlayerBet secondBet = new PlayerBet(secondPlayer, Bet.of(79));

        List<PlayerBet> bets = List.of(firstBet, secondBet);
        Game game = new Game(new Shoe(List.of(deck)), List.of(firstPlayer, secondPlayer));
        game.placePlayerBets(bets);
        game.initialDeal();
        return game;
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck) {
        Bet firstBet = Bet.of(23);
        Bet secondBet = Bet.of(79);
        List<Bet> bets = List.of(firstBet, secondBet);
        Game game = new Game(new Shoe(List.of(deck)), List.of(new PlayerId(99), new PlayerId(98)));
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck, PlayerBet firstPlayerBet, PlayerBet secondPlayerBet) {
        List<PlayerBet> bets = List.of(firstPlayerBet, secondPlayerBet);
        // FIXME: playerId should not be hardcoded
        Game game = new Game(new Shoe(List.of(deck)), List.of(new PlayerId(99), new PlayerId(98)));
        game.placePlayerBets(bets);
        game.initialDeal();
        return game;
    }

    public static Game createMultiPlayerGamePlaceBetsInitialDeal(int playerCount, Deck deck) {
        List<PlayerId> playerIds = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            playerIds.add(new PlayerId(i + 50));
        }

        return createMultiPlayerGamePlaceBetsInitialDeal(playerIds, deck);
    }

    public static Game createMultiPlayerGamePlaceBetsInitialDeal(List<PlayerId> playerIds, Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), playerIds);
        List<PlayerBet> bets = new ArrayList<>();
        for (int i = 0; i < playerIds.size(); i++) {
            bets.add(new PlayerBet(playerIds.get(i), Bet.of(11 * (i + 1))));
        }
        game.placePlayerBets(bets);
        game.initialDeal();
        return game;
    }

    public static List<PlayerBet> createPlayerBets(PlayerCount playerCount) {
        List<PlayerBet> playerBets = new ArrayList<>();
        for (int i = 0; i < playerCount.playerCount(); i++) {
            PlayerBet playerBet = new PlayerBet(
                    new PlayerId((i * 10) + 1),
                    Bet.of((i + 1) * 6));
            playerBets.add(playerBet);
        }
        return playerBets;
    }
}
