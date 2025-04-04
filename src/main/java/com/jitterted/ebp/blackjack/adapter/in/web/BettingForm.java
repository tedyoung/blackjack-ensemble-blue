package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountFinder;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerBet;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BettingForm {

    private static final String INITIAL_BET_OF_ZERO = "0";

    // maps Player ID to Amount of Bet
    private Map<String, String> playerIdToBets;

    // add map of Player ID to Name
    private Map<String, String> playerIdToNames;

    public BettingForm() {
    }

    public BettingForm(Map<String, String> playerIdToBets, Map<String, String> playerIdToNames) {
        this.playerIdToBets = playerIdToBets;
        this.playerIdToNames = playerIdToNames;
    }

    public static BettingForm zeroBetsFor(PlayerAccountFinder playerAccountFinder,
                                          List<PlayerId> playerIds) {
        Map<String, String> playerBets = new HashMap<>();
        Map<String, String> playerIdToNames = new HashMap<>();
        for (PlayerId playerId : playerIds) {
            String playerIdAsString = String.valueOf(playerId.id());
            playerBets.put(playerIdAsString, INITIAL_BET_OF_ZERO);
            PlayerAccount playerAccount = playerAccountFinder.find(playerId).orElseThrow();
            playerIdToNames.put(
                    playerIdAsString,
                    playerAccount.name() + " $" + playerAccount.balance());
        }
        return new BettingForm(playerBets, playerIdToNames);
    }

    // Transforms the Adapter/HTML Form content to Domain Objects for us
    public List<PlayerBet> getPlayerBets() {
        List<PlayerBet> playerBets = new ArrayList<>();
        playerIdToBets.forEach((key, value) -> {
            int id = Integer.parseInt(key);
            int amount = Integer.parseInt(value);
            playerBets.add(new PlayerBet(PlayerId.of(id), Bet.of(amount)));
        });
        return playerBets;
    }

    public Map<String, String> getPlayerIdToBets() {
        return playerIdToBets;
    }

    public void setPlayerIdToBets(Map<String, String> playerIdToBets) {
        this.playerIdToBets = playerIdToBets;
    }


    public Map<String, String> getPlayerIdToNames() {
        return playerIdToNames;
    }

    void validateBets(BindingResult bindingResult) {
        getPlayerIdToBets().forEach((playerId, betAmount) -> {
            try {
                int amount = Integer.parseInt(betAmount);
                if (!Bet.isValid(amount)) {
                    bindingResult.rejectValue("playerIdToBets[" + playerId + "]",
                                              "bet.amount.out.of.bounds",
                                             "Bet amount must be greater than zero");
                }
            } catch (NumberFormatException e) {
                bindingResult.rejectValue("playerIdToBets[" + playerId + "]",
                                          "bet.amount.invalid",
                                         "Bet amount must be a valid number");
            }
        });
    }
}
