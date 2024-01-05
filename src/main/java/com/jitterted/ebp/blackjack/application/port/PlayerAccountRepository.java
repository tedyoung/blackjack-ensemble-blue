package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;

public class PlayerAccountRepository {

    private final int id;

    public PlayerAccountRepository() {
        this.id = 0;
    }

    public PlayerAccountRepository(int id) {
        this.id = id;
    }

    public PlayerAccount load(PlayerId playerId) {
        var account = PlayerAccount.register("");
        account.setId(playerId);
        return account;
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        playerAccount.setId(PlayerId.of(42));
        return playerAccount;
    }
}
