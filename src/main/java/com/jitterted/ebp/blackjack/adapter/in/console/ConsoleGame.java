package com.jitterted.ebp.blackjack.adapter.in.console;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameService;
import org.fusesource.jansi.Ansi;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleGame {

  private final GameService gameService;
  private Game game;

  public ConsoleGame(GameService gameService) {
    this.gameService = gameService;
  }

  public void start() {
    displayWelcomeScreen();

    gameService.createGame();
    game = gameService.currentGame();

    game.initialDeal();

    playerPlays();

    displayFinalGameState();

    String outcome = ConsoleGameOutcome.of(game.determineOutcome());
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
    System.out.println(ConsoleHand.cardsAsString(game.playerHand()));
    System.out.println(" (" + game.playerHand().value() + ")");
  }

  private void displayFinalGameState() {
    System.out.print(ansi().eraseScreen().cursor(1, 1));
    System.out.println("Dealer has: ");
    System.out.println(ConsoleHand.cardsAsString(game.dealerHand()));
    System.out.println(" (" + game.dealerHand().value() + ")");

    System.out.println();
    System.out.println("Player has: ");
    System.out.println(ConsoleHand.cardsAsString(game.playerHand()));
    System.out.println(" (" + game.playerHand().value() + ")");
  }

  private void playerPlays() {
    while (!game.isPlayerDone()) {
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
