package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;

import java.util.List;

public class GameInProgressView {
    private List<String> dealerCards;
    private List<String> playerCards;
    private int playerId;

    public static GameInProgressView of(Game game) {
        GameInProgressView view = new GameInProgressView();
        view.dealerCards = CardMapper.cardsAsString(game.dealerHand().cards());
        view.playerCards = CardMapper.cardsAsString(game.currentPlayerCards());
        view.playerId = game.currentPlayerId();
        return view;
    }

    public List<String> getDealerCards() {
        return dealerCards;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    public int getPlayerId() {
        return playerId;
    }
}
