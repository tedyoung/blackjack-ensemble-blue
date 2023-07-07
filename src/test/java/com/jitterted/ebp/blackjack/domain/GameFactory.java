package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;

public class GameFactory {
    public static Game createOnePlayerGame() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        Shoe shoe = new Shoe(List.of(deck));
        return new Game(shoe, PlayerCount.of(1));
    }

    public static Game createOnePlayerGamePlaceBets() {
        Game game = createOnePlayerGame();
        List<Bet> bets = List.of(Bet.of(42));

        game.placeBets(bets);
        return game;
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerCount.of(1));
        List<Bet> bets = List.of(Bet.of(42));
        game.placeBets(bets);
        return game;
    }

    public static Game createOnePlayerGamePlaceBetsInitialDeal(Deck deck) {
        Game game = createOnePlayerGamePlaceBets(deck);
        game.initialDeal();
        return game;
    }

    public static Game createTwoPlayerGamePlaceBets(Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerCount.of(2));
        List<Bet> bets = List.of(Bet.of(11), Bet.of(22));
        game.placeBets(bets);
        return game;
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerCount.of(2));
        List<Bet> bets = List.of(Bet.of(11), Bet.of(22));
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }

    public static Game createGamePlaceBetsInitialDeal(int playerCount, Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerCount.of(playerCount));
        List<Bet> bets = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            bets.add(Bet.of(11 * i));
        }
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }

    public static List<PlayerBet> createBets(int numberOfBets) {
        List<PlayerBet> playerBets = new ArrayList<>();
        for (int i = 0; i < numberOfBets; i++) {

        }
    }

    public static List<PlayerBet> createThreeBets() {
        return List.of(
                new PlayerBet(new PlayerId(23), Bet.of(6)),
                new PlayerBet(new PlayerId(34), Bet.of(7)),
                new PlayerBet(new PlayerId(56), Bet.of(8)));
    }
}
