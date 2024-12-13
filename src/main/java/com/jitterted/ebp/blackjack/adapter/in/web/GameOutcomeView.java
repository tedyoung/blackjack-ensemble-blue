package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountFinder;
import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.domain.Game;

import java.util.List;
import java.util.stream.Collectors;

public class GameOutcomeView {

    private List<PlayerOutcomeView> playerOutcomes;
    private List<String> dealerCards;

    public static GameOutcomeView of(Game game, PlayerAccountFinder playerAccountFinder) {
        GameOutcomeView gameOutcomeView = new GameOutcomeView();
        gameOutcomeView.playerOutcomes = game.playerResults().stream()
                                             .map(playerResult -> PlayerOutcomeView.from(playerResult, playerAccountFinder))
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
