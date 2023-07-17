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
        Shoe shoe = new Shoe(List.of(deck));
        return createOnePlayerGamePlaceBets(shoe);
    }

    public static Game createOnePlayerGamePlaceBets(Shoe shoe) {
        Game game = new Game(shoe, PlayerCount.of(1));
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
        Bet firstBet = Bet.of(23);
        Bet secondBet = Bet.of(79);
        return createTwoPlayerGamePlaceBetsInitialDeal(deck, firstBet, secondBet);
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck, Bet firstBet, Bet secondBet) {
        List<Bet> bets = List.of(firstBet, secondBet);
        Game game = new Game(new Shoe(List.of(deck)), PlayerCount.of(2));
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }

    public static Game createMultiPlayerGamePlaceBetsInitialDeal(int playerCount, Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerCount.of(playerCount));
        List<Bet> bets = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            bets.add(Bet.of(11 * i));
        }
        game.placeBets(bets);
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
