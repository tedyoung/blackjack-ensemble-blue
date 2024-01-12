package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.HashMap;
import java.util.Map;

public class PlayerAccountRepository {

    private final int nextId;
    private Map<PlayerId, PlayerAccount> repository = new HashMap<>();

    public PlayerAccountRepository() {
        this.nextId = 0;
    }

    private PlayerAccountRepository(int id) {
        this.nextId = id;
    }

    public static PlayerAccountRepository withNextId(int id) {
        return new PlayerAccountRepository(id);
    }

    public PlayerAccount load(PlayerId playerId) {
        return repository.get(playerId);
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        if (playerAccount.getId() == null) {
            playerAccount.setId(PlayerId.of(nextId));
        }
        repository.put(playerAccount.getId(), playerAccount);
        return playerAccount;
    }
}
