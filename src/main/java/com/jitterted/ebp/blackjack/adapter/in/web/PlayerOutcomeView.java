package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.PlayerResult;

import java.util.List;

public class PlayerOutcomeView {

    private int playerId;
    private List<String> playerCards;
    private String playerOutcome;
    private String betOutcome;

    public static PlayerOutcomeView of(PlayerResult playerResult) {
        PlayerOutcomeView playerOutcomeView = new PlayerOutcomeView();
        playerOutcomeView.playerId = playerResult.id();
        playerOutcomeView.playerCards = CardMapper.cardsAsString(playerResult.cards());
        playerOutcomeView.playerOutcome = playerResult.outcome().toString();
        playerOutcomeView.betOutcome = "You bet 10 and got back 25"; // TODO: this is currently hard-coded
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

    public String getBetOutcome() {
        return betOutcome;
    }
}
