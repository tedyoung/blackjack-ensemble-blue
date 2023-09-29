package com.jitterted.ebp.blackjack.adapter.in.web;

import java.util.List;

public class NewGameForm {
    private List<String> playersPlaying;

    public NewGameForm(List<String> playersPlaying) {
        this.playersPlaying = playersPlaying;
    }

    public List<String> getPlayersPlaying() {
        return playersPlaying;
    }

    public void setPlayersPlaying(List<String> playersPlaying) {
        this.playersPlaying = playersPlaying;
    }
}
