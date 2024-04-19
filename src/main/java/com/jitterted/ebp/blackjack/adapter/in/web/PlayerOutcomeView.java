package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.PlayerResult;

import java.util.List;

public class PlayerOutcomeView {

    private int playerId;
    private List<String> playerCards;
    private String playerOutcome;
    private String betOutcome;

    public static PlayerOutcomeView from(PlayerResult playerResult) {
        PlayerOutcomeView playerOutcomeView = new PlayerOutcomeView();
        playerOutcomeView.playerId = playerResult.playerId().id();
        playerOutcomeView.playerCards = CardMapper.cardsAsString(playerResult.cards());
        playerOutcomeView.playerOutcome = playerResult.outcome().toString();
        playerOutcomeView.betOutcome = betOutcome(playerResult);
        return playerOutcomeView;
    }

    private static String betOutcome(PlayerResult playerResult) {
        int betAmount = playerResult.bet().amount();
        int payout = playerResult.payout();
        return "You bet " + betAmount + " and got back " + payout;
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
