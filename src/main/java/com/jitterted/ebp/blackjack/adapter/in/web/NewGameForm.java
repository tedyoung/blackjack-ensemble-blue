package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.List;

@Deprecated // use PlayerSelectionForm instead
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

    // add isValid() or a validate() to ensure all are Integers

    public List<PlayerId> getPlayerIds() {
        return playersPlaying.stream()
                      .map(Integer::parseInt)
                      .map(PlayerId::of)
                      .toList();
    }
}
