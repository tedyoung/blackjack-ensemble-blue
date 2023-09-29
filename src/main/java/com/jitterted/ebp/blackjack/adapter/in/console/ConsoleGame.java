package com.jitterted.ebp.blackjack.adapter.in.console;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Shoe;
import org.fusesource.jansi.Ansi;

import java.util.List;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleGame {

    private final GameService gameService;
    private Shoe shoe;
    private Game game;

    public ConsoleGame(GameService gameService, Shoe shoe) {
        this.gameService = gameService;
        this.shoe = shoe;
    }

    public void start() {
        displayWelcomeScreen();

        gameService.createGame(List.of(PlayerId.of(1)), shoe);
        game = gameService.currentGame();

        game.initialDeal();

        playerPlays();

        displayFinalGameState();

        String outcome = ConsoleGameOutcome.of(game.currentPlayerOutcome());
        System.out.println(outcome);

        resetScreen();
    }

    private void resetScreen() {
        System.out.println(ansi().reset());
    }

    private void displayWelcomeScreen() {
        System.out.println(ansi()
                                   .bgBright(Ansi.Color.WHITE)
                                   .eraseScreen()
                                   .cursor(1, 1)
                                   .fgGreen().a("Welcome to")
                                   .fgRed().a(" Jitterted's")
                                   .fgBlack().a(" BlackJack"));
    }

    private void displayBackOfCard() {
        System.out.print(
                ansi()
                        .cursorUp(7)
                        .cursorRight(12)
                        .a("┌─────────┐").cursorDown(1).cursorLeft(11)
                        .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
                        .a("│░ J I T ░│").cursorDown(1).cursorLeft(11)
                        .a("│░ T E R ░│").cursorDown(1).cursorLeft(11)
                        .a("│░ T E D ░│").cursorDown(1).cursorLeft(11)
                        .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
                        .a("└─────────┘"));
    }

    private void displayGameState() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        System.out.println(ConsoleHand.displayFirstCard(game.dealerHand())); // first card is Face Up

        // second card is the hole card, which is hidden
        displayBackOfCard();

        System.out.println();
        System.out.println("Player has: ");
        System.out.println(ConsoleHand.cardsAsString(game.currentPlayerCards()));
        System.out.println(" (" + game.currentPlayerHandValue() + ")");
    }

    private void displayFinalGameState() {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        System.out.println(ConsoleHand.cardsAsString(game.dealerHand().cards()));
        System.out.println(" (" + game.dealerHand().value() + ")");

        System.out.println();
        System.out.println("Player has: ");
        System.out.println(ConsoleHand.cardsAsString(game.currentPlayerCards()));
        System.out.println(" (" + game.currentPlayerHandValue() + ")");
    }

    private void playerPlays() {
        while (!game.isGameOver()) {
            displayGameState();
            String command = inputFromPlayer();
            handle(command);
        }
    }

    private String inputFromPlayer() {
        System.out.println("[H]it or [S]tand?");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        return command;
    }

    private void handle(String command) {
        if (command.toLowerCase().startsWith("h")) {
            game.playerHits();
        } else if (command.toLowerCase().startsWith("s")) {
            game.playerStands();
        }
    }

}
