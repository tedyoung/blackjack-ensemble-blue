package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.console.ConsoleGame;
import com.jitterted.ebp.blackjack.adapter.out.shuffler.RandomShuffler;
import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.OrderedDeck;
import com.jitterted.ebp.blackjack.domain.Shoe;

import java.util.List;

public class Blackjack {

    // This the Application Assembler that configures and starts the application
    public static void main(String[] args) {
        final Deck deck = new OrderedDeck();
        final List<Deck> deckFactory = List.of(deck);
        GameService gameService = new GameService(game -> { }, game -> {}, new RandomShuffler(), null);
        ConsoleGame consoleGame = new ConsoleGame(gameService, new Shoe(deckFactory));
        consoleGame.start();
    }
}
