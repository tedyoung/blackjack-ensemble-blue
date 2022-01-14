package com.jitterted.ebp.blackjack.adapter.out.gamemonitor;

import com.jitterted.ebp.blackjack.domain.Game;

public class GameResultDto {
    private final String playerName;
    private final String outcome;
    private final String playerHandValue;
    private final String dealerHandValue;

    public GameResultDto(String playerName, String outcome, String playerHandValue, String dealerHandValue) {
        this.playerName = playerName;
        this.outcome = outcome;
        this.playerHandValue = playerHandValue;
        this.dealerHandValue = dealerHandValue;
    }

    public static GameResultDto of(Game game) {
        GameResultDto gameResultDto = new GameResultDto(
                "Ted",
                game.currentPlayerOutcome().toString(), // stringified GameOutcome to API
                String.valueOf(game.currentPlayerHandValue()),
                String.valueOf(game.dealerHand().value()));
        return gameResultDto;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getPlayerHandValue() {
        return playerHandValue;
    }

    public String getDealerHandValue() {
        return dealerHandValue;
    }

}
