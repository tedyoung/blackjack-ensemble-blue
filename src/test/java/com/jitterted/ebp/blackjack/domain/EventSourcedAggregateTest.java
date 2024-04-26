package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EventSourcedAggregateTest {

    @Test
    void afterLoadingAggregateFreshEventsIsEmpty() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"),
                                                  new MoneyDeposited(10));
        EventSourcedAggregate playerAccount = PlayerAccount.reconstitute(
                PlayerId.irrelevantPlayerId(),
                events);

        assertThat(playerAccount.freshEvents())
                .isEmpty();
    }

    @Test
    void generatedEventsAreApplied() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"),
                                                  new MoneyDeposited(10));
        PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

        playerAccount.deposit(5);

        assertThat(playerAccount.balance())
                .isEqualTo(15);
    }

    @Test
    void newlyCreatedPlayerAccountHasLastEventIdZero() throws Exception {
        PlayerAccount account = PlayerAccount.register("Irrelevant Name");

        assertThat(account.lastEventId())
                .isZero();
    }

    @Test
    void newPlayerAccountWithFreshEventsHasLastEventIdZero() {
        PlayerAccount account = PlayerAccount.register("Irrelevant Name");

        account.bet(10);

        assertThat(account.lastEventId())
                .isZero();
    }

    @Test
    void aggregateRemembersLastEventIdLoaded() {
        List<PlayerAccountEvent> events = List.of(
                new PlayerRegistered("Irrelevant Name"), // eventId = 1
                new MoneyDeposited(13));                // eventId = 2
        PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

        int actual = playerAccount.lastEventId();

        assertThat(actual)
                .as("eventId is 1-based, so the 'lastEventId', which is the second one, should be 2")
                .isEqualTo(2);
    }

    @Test
    void aggregateRecordsFreshEvents() {
        List<PlayerAccountEvent> events = List.of(
                new PlayerRegistered("Irrelevant Name"),
                new MoneyDeposited(13));
        PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

        playerAccount.bet(7);

        assertThat(playerAccount.freshEvents())
                .containsExactly(new MoneyBet(7));
    }

    @Test
    void playerAccountRecordsAnId() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"));
        PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

        playerAccount.setPlayerId(PlayerId.of(4));

        assertThat(playerAccount.getPlayerId())
                .isEqualTo(PlayerId.of(4));
    }

    @Nested class Equality {

        @Test
        void twoPlayerAccountsWithNullIdsAreNotEqual() {
            TestableAggregate first = new TestableAggregate(null);
            TestableAggregate second = new TestableAggregate(null);

            assertThat(first)
                    .isNotEqualTo(second);
        }

        @Test
        void twoAggregatesWithTheSameIdAreEqual() {
            TestableAggregate first = new TestableAggregate(PlayerId.of(777));
            TestableAggregate second = new TestableAggregate(PlayerId.of(777));

            assertThat(first)
                    .isEqualTo(second);
        }

        @Test
        void twoAggregatesWithDifferentIdsAreNotEqual() {
            TestableAggregate first = new TestableAggregate(PlayerId.of(778));
            TestableAggregate second = new TestableAggregate(PlayerId.of(777));

            assertThat(first)
                    .isNotEqualTo(second);
        }

        static class TestableAggregate extends EventSourcedAggregate {

            public TestableAggregate(PlayerId playerId) {
                super(playerId, 42);
            }

            @Override
            public void apply(PlayerAccountEvent event) {
            }
        }
    }

}