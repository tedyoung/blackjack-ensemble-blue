package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.console.ConsoleGame;
import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.DeckFactory;

public class Blackjack {

    // This the Application Assembler that configures and starts the application
    public static void main(String[] args) {
        final Deck deck = new Deck();
        GameService gameService = new GameService(new DeckFactory(deck));
        ConsoleGame consoleGame = new ConsoleGame(gameService);
        consoleGame.start();
    }
}
