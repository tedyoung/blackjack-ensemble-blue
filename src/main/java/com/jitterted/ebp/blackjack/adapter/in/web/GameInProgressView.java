package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;

import java.util.List;
import java.util.stream.Collectors;

public class GameInProgressView {

    private int playerId;
    private List<String> dealerCards;
    private List<String> playerCards;
    private List<String> playerEvents;

    public static GameInProgressView of(Game game) {
        GameInProgressView view = new GameInProgressView();

        view.dealerCards = CardMapper.dealerInProgressCardsAsString(game.dealerHand().cards());

        view.playerCards = CardMapper.cardsAsString(game.currentPlayerCards());
        view.playerId = game.currentPlayerId();
        view.playerEvents = game.events().stream()
                                .map(event -> event.id() + ": " + event.reasonDone())
                                .collect(Collectors.toList());
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

    public List<String> getPlayerEvents() {
        return playerEvents;
    }
}
