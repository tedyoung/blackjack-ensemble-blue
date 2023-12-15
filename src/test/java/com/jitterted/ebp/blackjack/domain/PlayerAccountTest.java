package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountTest {

    @Nested
    class EventHandling {
        /**
         * Eventually this behavior would move to a base "Event Sourced Aggregate" class
         * that is responsible for managing the events, and its ID
         */
        @Test
        void playerAccountRecordsEvents() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(),
                                                      new MoneyDeposited(10));
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.events())
                    .containsExactly(new PlayerRegistered(),
                                     new MoneyDeposited(10));
        }

        @Test
        void playerAccountRecordsNewEvents() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(),
                                                      new MoneyDeposited(10));
            PlayerAccount playerAccount = new PlayerAccount(events);

            playerAccount.deposit(20);

            assertThat(playerAccount.events())
                    .hasSize(3);
        }

        @Test
        void generatedEventsAreApplied() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(),
                                                      new MoneyDeposited(10));
            PlayerAccount playerAccount = new PlayerAccount(events);

            playerAccount.deposit(5);

            assertThat(playerAccount.balance())
                    .isEqualTo(15);
        }
    }

    @Nested
    class CommandsGenerateEvents {

        @Test
        void registeringPlayerEmitsPlayerRegistered() {
            PlayerAccount playerAccount = PlayerAccount.register();

            Stream<PlayerAccountEvent> events = playerAccount.events();

            assertThat(events)
                    .containsExactly(new PlayerRegistered());
        }

        @Test
        void depositEmitsMoneyDeposited() {
            PlayerAccount playerAccount = PlayerAccount.register();

            playerAccount.deposit(55);

            assertThat(playerAccount.events())
                    .containsExactly(new PlayerRegistered(),
                                     new MoneyDeposited(55));
        }

    }

    @Nested
    class EventsProjectState {
        @Test
        void newPlayerAccountHasZeroBalance() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered());
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.balance())
                    .isZero();
        }

        @Test
        void moneyDeposited10HasBalance10() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(),
                                                      new MoneyDeposited(10));
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.balance())
                    .isEqualTo(10);
        }

        @Test
        void moneyDepositedMultipleTimes() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(),
                                                      new MoneyDeposited(53),
                                                      new MoneyDeposited(25));
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.balance())
                    .isEqualTo(53 + 25);
        }
    }

}
