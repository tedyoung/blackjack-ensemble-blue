package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EventSourcedAggregateTest {

    @Test
    void playerAccountRecordsEvents() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"),
                                                  new MoneyDeposited(10));
        EventSourcedAggregate playerAccount = new PlayerAccount(null, events);

        assertThat(playerAccount.events())
                .containsExactly(new PlayerRegistered("Jane"),
                                 new MoneyDeposited(10));
    }

    @Test
    void playerAccountRecordsNewEvents() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"),
                                                  new MoneyDeposited(10));
        PlayerAccount playerAccount = new PlayerAccount(null, events);

        playerAccount.deposit(20);

        assertThat(playerAccount.events())
                .hasSize(3);
    }

    @Test
    void generatedEventsAreApplied() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"),
                                                  new MoneyDeposited(10));
        PlayerAccount playerAccount = new PlayerAccount(null, events);

        playerAccount.deposit(5);

        assertThat(playerAccount.balance())
                .isEqualTo(15);
    }

    @Test
    void playerAccountRecordsAnId() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"));
        PlayerAccount playerAccount = new PlayerAccount(null, events);

        playerAccount.setId(PlayerId.of(4));

        assertThat(playerAccount.getId())
                .isEqualTo(PlayerId.of(4));
    }

}