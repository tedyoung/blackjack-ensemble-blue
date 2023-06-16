package com.jitterted.ebp.blackjack.domain;

import com.jitterted.ebp.blackjack.application.PlayerFactory;

import java.util.ArrayList;
import java.util.List;

public class GameFactory {
    public static Game createOnePlayerGame() {
        Deck deck = StubDeckBuilder.buildOnePlayerFixedDeck();
        return new Game(new Shoe(List.of(deck)), PlayerFactory.create(PlayerCount.of(1)));
    }

    public static Game createOnePlayerGamePlaceBets() {
        Game game = createOnePlayerGame();
        List<Bet> bets = List.of(Bet.of(42));

        game.placeBets(bets);
        return game;
    }

    public static Game createOnePlayerGamePlaceBets(Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerFactory.create(PlayerCount.of(1)));
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
        final List<Deck> deckFactory = List.of(deck);
        Game game = new Game(new Shoe(deckFactory), PlayerFactory.create(PlayerCount.of(2)));
        List<Bet> bets = List.of(Bet.of(11), Bet.of(22));
        game.placeBets(bets);
        return game;
    }

    public static Game createTwoPlayerGamePlaceBetsInitialDeal(Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerFactory.create(PlayerCount.of(2)));
        List<Bet> bets = List.of(Bet.of(11), Bet.of(22));
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }

    public static Game createGamePlaceBetsInitialDeal(int playerCount, Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), PlayerFactory.create(PlayerCount.of(playerCount)));
        List<Bet> bets = new ArrayList<>();
        for (int i = 1; i <= playerCount; i++) {
            bets.add(Bet.of(11 * i));
        }
        game.placeBets(bets);
        game.initialDeal();
        return game;
    }
}
