package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelectionForm {

    private final List<PlayerAccountView> players;
    private List<Long> playersPlaying = new ArrayList<>();

    public PlayerSelectionForm(List<PlayerAccountView> players) {
        this.players = players;
    }

    public List<PlayerAccountView> getPlayers() {
        return players;
    }

    public List<Long> getPlayersPlaying() {
        return playersPlaying;
    }

    public void setPlayersPlaying(List<Long> playersPlaying) {
        this.playersPlaying = playersPlaying;
    }

    public List<PlayerId> getPlayerIds() {
        return playersPlaying.stream()
                             .map(id -> PlayerId.of(id.intValue()))
                             .toList();
    }
}
