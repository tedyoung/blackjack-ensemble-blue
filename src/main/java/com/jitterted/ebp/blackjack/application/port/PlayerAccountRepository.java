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
        return PlayerAccount.reconstitute(playerId, events);
    }

    public PlayerAccount save(PlayerAccount playerAccount) {
        if (playerAccount.getPlayerId() == null) {
            playerAccount.setPlayerId(PlayerId.of(nextId++));
        }
        List<EventDto> existingEventDtos = eventDtosByPlayer.computeIfAbsent(playerAccount.getPlayerId(),
                                                                             (_) -> new ArrayList<>());
        // TODO: Do we want to do this, or use EventSourcedAggregates.lastEventId()?
        int nextEventId = existingEventDtos.isEmpty() ? 0 : existingEventDtos.getLast().getEventId() + 1;
        AtomicInteger index = new AtomicInteger(nextEventId);
        List<EventDto> freshEventDtos = playerAccount.freshEvents()
                                                     .map(event -> EventDto.from(
                                                             playerAccount.getPlayerId().id(),
                                                             index.getAndIncrement(),
                                                             event))
                                                     .toList();
        existingEventDtos.addAll(freshEventDtos);
        ensureIncreasingUniqueIds(existingEventDtos);
        return playerAccount;
    }

    private void ensureIncreasingUniqueIds(List<EventDto> existingEventDtos) {
        for (int i = 0; i < existingEventDtos.size(); i++) {
            if (existingEventDtos.get(i).getEventId() != i) {
                throw new IllegalStateException();
            }
        }
    }
}
