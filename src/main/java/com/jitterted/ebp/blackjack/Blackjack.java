package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.console.ConsoleGame;
import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.DeckFactory;
import com.jitterted.ebp.blackjack.domain.Shoe;
import com.jitterted.ebp.blackjack.domain.ShuffledDeck;

public class Blackjack {

    // This the Application Assembler that configures and starts the application
    public static void main(String[] args) {
        final Deck deck = new ShuffledDeck();
        final DeckFactory deckFactory = DeckFactory.createForTest(deck);
        GameService gameService = new GameService(new Shoe(deckFactory));
        ConsoleGame consoleGame = new ConsoleGame(gameService);
        consoleGame.start();
    }
}
