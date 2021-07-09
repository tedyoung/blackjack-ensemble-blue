package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Game;

import java.util.List;
import java.util.stream.Collectors;

public class GameView {
    private List<String> dealerCards;
    private List<String> playerCards;

    public static GameView of(Game game) {
        GameView gameView = new GameView();
        gameView.dealerCards = cardsAsString(game.dealerHand().cards());
        gameView.playerCards = cardsAsString(game.playerHand().cards());
        return gameView;
    }

    public List<String> getDealerCards() {
        return dealerCards;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    private static List<String> cardsAsString(List<Card> cards) {
        return cards.stream()
                    .map(GameView::displayOf)
                    .collect(Collectors.toList());
    }

    private static String displayOf(Card card) {
        return card.rank().display() + card.suit().symbol();
    }
}
