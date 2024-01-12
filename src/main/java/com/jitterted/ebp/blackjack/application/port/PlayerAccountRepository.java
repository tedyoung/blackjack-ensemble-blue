package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.HashMap;
import java.util.Map;

public class PlayerAccountRepository {

    private final int id;
    private PlayerAccount account;
    private Map<PlayerId, PlayerAccount> repository = new HashMap<>();

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
        return this.account;
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        this.account = playerAccount;
        if (playerAccount.getId() == null) {
            playerAccount.setId(PlayerId.of(id));
        }
        return playerAccount;
    }
}
