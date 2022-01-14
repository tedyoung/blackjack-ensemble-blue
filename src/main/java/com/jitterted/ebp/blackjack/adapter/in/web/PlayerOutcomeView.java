package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.PlayerResult;

import java.util.List;

public class PlayerOutcomeView {

    private int playerId;
    private List<String> playerCards;
    private String playerOutcome;

    public static PlayerOutcomeView of(PlayerResult playerResult) {
        PlayerOutcomeView playerOutcomeView = new PlayerOutcomeView();
        playerOutcomeView.playerId = playerResult.id();
        playerOutcomeView.playerCards = CardMapper.cardsAsString(playerResult.cards());
        playerOutcomeView.playerOutcome = playerResult.outcome().toString();
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
