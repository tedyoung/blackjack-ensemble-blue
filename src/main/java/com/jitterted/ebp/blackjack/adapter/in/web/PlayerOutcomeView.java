package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Hand;
import com.jitterted.ebp.blackjack.domain.Player;

import java.util.List;

public class PlayerOutcomeView {

    private int playerId;
    private List<String> playerCards;
    private String playerOutcome;

    public static PlayerOutcomeView of(Hand dealerHand, Player player) {
        PlayerOutcomeView playerOutcomeView = new PlayerOutcomeView();
        playerOutcomeView.playerId = player.id();
        playerOutcomeView.playerCards = CardMapper.cardsAsString(player.cards());
        playerOutcomeView.playerOutcome = player.outcome(dealerHand).toString();
        return playerOutcomeView;
    }

    public int getPlayerId() {
        return playerId;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    public String getPlayerOutcome() {
        return playerOutcome;
    }

}
