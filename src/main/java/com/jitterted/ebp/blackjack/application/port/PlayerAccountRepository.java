package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;

public class PlayerAccountRepository {

    private final int id;

    public PlayerAccountRepository() {
        this.id = 0;
    }

    private PlayerAccountRepository(int id) {
        this.id = id;
    }

    public static PlayerAccountRepository withNextId(int id) {
        return new PlayerAccountRepository(id);
    }

    public PlayerAccount load(PlayerId playerId) {
        var account = PlayerAccount.register("");
        account.setId(playerId);
        return account;
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        playerAccount.setId(PlayerId.of(id));
        return playerAccount;
    }
}
