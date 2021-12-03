package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerOutcomeView {

    private int playerId;
    private List<String> playerCards;
    private Player player;
    private Hand dealerHand;

    public static PlayerOutcomeView of(Hand dealerBustedHand, Player player) {
        PlayerOutcomeView playerOutcomeView = new PlayerOutcomeView();
        playerOutcomeView.playerId = player.id();
        playerOutcomeView.playerCards = cardsAsString(player.cards());
        playerOutcomeView.player = player;
        playerOutcomeView.dealerHand = dealerBustedHand;
        return playerOutcomeView;
    }

    public int getPlayerId() {
        return playerId;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    private static List<String> cardsAsString(List<Card> cards) {
        return cards.stream()
                    .map(PlayerOutcomeView::displayOf)
                    .collect(Collectors.toList());
    }

    private static String displayOf(Card card) {
        return card.rank().display() + card.suit().symbol();
    }

    public String getPlayerOutcome() {
        return player.outcome(dealerHand).toString();
    }
}
