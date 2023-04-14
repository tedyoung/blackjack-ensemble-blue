package com.jitterted.ebp.blackjack.domain;

import java.util.List;

public class GameFactory {
    public static Game createOnePlayerGame() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        return new Game(PlayerCount.of(1), new Shoe(List.of(deck)));
    }

    public static Game createOnePlayerGamePlaceBets() {
        Game game = createOnePlayerGame();
        List<Bet> bets = List.of(Bet.of(42));

        game.placeBets(bets);
        return game;
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck) {
        Game game = new Game(PlayerCount.of(1), new Shoe(List.of(deck)));
        List<Bet> bets = List.of(Bet.of(42));
        game.placeBets(bets);
        return game;
    }

    public static Game createOnePlayerGamePlaceBetsInitialDeal(Deck deck) {
        Game game = createOnePlayerGamePlaceBets(deck);
        game.initialDeal();
        return game;
    }

    static Game twoPlayerGamePlaceBetsInitialDeal(Deck deck) {
        Game game = new Game(PlayerCount.of(2), new Shoe(List.of(deck)));
        List<Bet> bets = List.of(Bet.of(11), Bet.of(22));
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }
}
