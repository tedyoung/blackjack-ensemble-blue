package com.jitterted.ebp.blackjack.adapter.in.console;

import com.jitterted.ebp.blackjack.domain.Hand;

import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleHand {
  public static String displayFirstCard(Hand hand) {
    return ConsoleCard.display(hand.cards().get(0));
  }

  public static String cardsAsString(Hand hand) {
    return hand.cards().stream()
               .map(ConsoleCard::display)
               .collect(Collectors.joining(
                    ansi().cursorUp(6).cursorRight(1).toString()));
  }
}
