package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerAccountRepository {

    private int nextId;
    private final Map<PlayerId, List<PlayerAccountEvent>> eventsByPlayer = new HashMap<>();
    private final Map<PlayerId, List<EventDto>> eventDtosByPlayer = new HashMap<>();

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
        if (eventDtosByPlayer.containsKey(playerId)) {
            List<PlayerAccountEvent> events = eventDtosByPlayer.get(playerId).stream()
                    .map(this::applesauce).toList();

            PlayerAccount playerAccount = new PlayerAccount(playerId, eventsByPlayer.get(playerId));
            return Optional.of(playerAccount);
        } else {
            return Optional.empty();
        }
    }

    private PlayerAccountEvent applesauce(EventDto eventDto) {
        return null;
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        if (playerAccount.getPlayerId() == null) {
            playerAccount.setPlayerId(PlayerId.of(nextId++));
        }
        eventsByPlayer.put(playerAccount.getPlayerId(), playerAccount.events().toList());
        AtomicInteger index = new AtomicInteger();
        eventDtosByPlayer.put(playerAccount.getPlayerId(),
                              playerAccount.events().map(
                                      event -> EventDto.createEventDto(playerAccount.getPlayerId().id(), index.getAndIncrement(), event)).toList());

        return playerAccount;
    }
}
