package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerAccountRepository {

    private int nextId;
    private final Map<PlayerId, PlayerAccount> repository = new HashMap<>();
    private final Map<PlayerId, List<PlayerAccountEvent>> eventsByPlayer = new HashMap<>();

    public PlayerAccountRepository() {
        this.nextId = 0;
    }

    private PlayerAccountRepository(int id) {
        this.nextId = id;
    }

    public static PlayerAccountRepository withNextId(int id) {
        return new PlayerAccountRepository(id);
    }

    public Optional<PlayerAccount> find(PlayerId playerId) {
        return Optional.ofNullable(repository.get(playerId));
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        if (playerAccount.getId() == null) {
            playerAccount.setId(PlayerId.of(nextId++));
        }
        repository.put(playerAccount.getId(), playerAccount);
        return playerAccount;
    }
}
