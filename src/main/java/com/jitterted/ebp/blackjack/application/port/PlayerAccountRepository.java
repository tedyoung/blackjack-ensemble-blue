package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerAccountEvent;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerAccountRepository {

    private int nextId;
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
        return Optional.ofNullable(eventDtosByPlayer.get(playerId))
                       .map(events -> mapPlayer(playerId, events));
    }

    private PlayerAccount mapPlayer(PlayerId playerId, List<EventDto> eventDtos) {
        List<PlayerAccountEvent> events = eventDtos
                .stream()
                .map(EventDto::toDomain)
                .toList();
        return new PlayerAccount(playerId, events);
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        if (playerAccount.getPlayerId() == null) {
            playerAccount.setPlayerId(PlayerId.of(nextId++));
        }
        // start with the playerAccount.lastEventId()+1 instead of at 0
        AtomicInteger index = new AtomicInteger(0);
        List<EventDto> existingEventDtos = eventDtosByPlayer.computeIfAbsent(playerAccount.getPlayerId(),
                                                                     (_) -> new ArrayList<>());
        List<EventDto> freshEventDtos = playerAccount.freshEvents()
                                           .map(event -> EventDto.from(
                                                   playerAccount.getPlayerId().id(),
                                                   index.getAndIncrement(),
                                                   event))
                                           .toList();
        existingEventDtos.addAll(freshEventDtos);
        for (int i = 0; i < existingEventDtos.size(); i++) {
            if (existingEventDtos.get(i).getEventId() != i) {
                throw new IllegalStateException();
            }
        }
        return playerAccount;
    }
}
