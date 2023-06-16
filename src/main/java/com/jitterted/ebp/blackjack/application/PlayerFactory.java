package com.jitterted.ebp.blackjack.application;

import com.jitterted.ebp.blackjack.domain.PlayerCount;
import com.jitterted.ebp.blackjack.domain.PlayerInGame;

import java.util.ArrayList;
import java.util.List;

public class PlayerFactory {
    public static List<PlayerInGame> create(PlayerCount numberOfPlayers) {
        List<PlayerInGame> players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers.playerCount(); i++) {
            players.add(new PlayerInGame(i));
        }
        return players;
    }
}
