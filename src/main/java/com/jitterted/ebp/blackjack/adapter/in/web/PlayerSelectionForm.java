package com.jitterted.ebp.blackjack.adapter.in.web;

import java.util.Collections;
import java.util.List;

// 1. Holds the players from which we can select to play the game
// 2. Accepts the players for which the checkbox was checked, i.e., they're playing
public class PlayerSelectionForm {

    public static PlayerSelectionForm of(List<PlayerAccountView> playerAccountViews) {
        PlayerSelectionForm form = new PlayerSelectionForm();

    }

    public List<PlayerAccountView> getPlayers() {
        return Collections.emptyList();
    }
}
