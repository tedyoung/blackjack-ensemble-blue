package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;

import java.util.List;
import java.util.stream.Collectors;

public class GameOutcomeView {

    private List<PlayerOutcomeView> playerOutcomes;
    private List<String> dealerCards;

    public static GameOutcomeView of(Game game) {
        GameOutcomeView gameOutcomeView = new GameOutcomeView();
        gameOutcomeView.playerOutcomes = game.getPlayers().stream()
                                              .map(player -> PlayerOutcomeView.of(game.dealerHand(), player))
                                              .collect(Collectors.toList());

        gameOutcomeView.dealerCards = CardMapper.cardsAsString(game.dealerHand().cards());
        return gameOutcomeView;
    }

    public List<PlayerOutcomeView> getPlayerOutcomes() {
        return playerOutcomes;
    }

    public List<String> getDealerCards() {
        return dealerCards;
    }
}
