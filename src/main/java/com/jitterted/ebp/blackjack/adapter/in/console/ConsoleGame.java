package com.jitterted.ebp.blackjack.adapter.in.console;

import com.jitterted.ebp.blackjack.domain.Game;
import org.fusesource.jansi.Ansi;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleGame {

  private final Game game;

  public ConsoleGame(Game game) {
    this.game = game;
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

  public void start() {
    displayWelcomeScreen();

    game.initialDeal();

    playerPlays();

    game.dealerTurn();

    displayFinalGameState();

    System.out.println(game.determineOutcome());

    resetScreen();
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
