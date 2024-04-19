package com.jitterted.ebp.blackjack.adapter.in.web;

import java.util.List;

// 1. Holds the players from which we can select to play the game
// 2. Accepts the players for which the checkbox was checked, i.e., they're playing
public class PlayerSelectionForm {

    private final List<PlayerAccountView> players;

    public PlayerSelectionForm(List<PlayerAccountView> players) {
        this.players = players;
    }

    public List<PlayerAccountView> getPlayers() {
        return players;
    }
}
