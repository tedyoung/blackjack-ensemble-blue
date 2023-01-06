package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.console.ConsoleGame;
import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.ShuffledDeck;

import java.util.List;

public class Blackjack {

    // This the Application Assembler that configures and starts the application
    public static void main(String[] args) {
        final Deck deck = new ShuffledDeck();
        final List<Deck> deckFactory = List.of(deck);
        GameService gameService = GameService.createForTest(new Shoe(deckFactory));
        ConsoleGame consoleGame = new ConsoleGame(gameService);
        consoleGame.start();
    }
}
