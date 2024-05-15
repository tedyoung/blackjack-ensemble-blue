package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;

public record PlayerAccountView(int playerId, String name) {

    static PlayerAccountView from(PlayerAccount playerAccount) {
        return new PlayerAccountView(playerAccount.getPlayerId().id(), playerAccount.name());
    }
}
