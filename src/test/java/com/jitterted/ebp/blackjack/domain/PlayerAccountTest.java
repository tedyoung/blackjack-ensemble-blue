package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountTest {

    @Nested
    class CommandsGenerateEvents {

        @Test
        void registeringPlayerEmitsPlayerRegistered() {
            EventSourcedAggregate playerAccount = PlayerAccount.register("John");

            Stream<PlayerAccountEvent> events = playerAccount.events();

            assertThat(events)
                    .containsExactly(new PlayerRegistered("John"));
        }

        @Test
        void depositEmitsMoneyDeposited() {
            PlayerAccount playerAccount = PlayerAccount.register("John");

            playerAccount.deposit(55);

            assertThat(playerAccount.events())
                    .containsExactly(new PlayerRegistered("John"),
                                     new MoneyDeposited(55));
        }

        @Test
        void betEmitsMoneyBet() {
            PlayerAccount playerAccount = PlayerAccount.register("John");
            playerAccount.deposit(55);

            playerAccount.bet(12);

            assertThat(playerAccount.events())
                    .containsExactly(new PlayerRegistered("John"),
                                     new MoneyDeposited(55),
                                     new MoneyBet(12));
        }
    }

    @Nested
    class EventsProjectState {

        @Test
        void newPlayerAccountHasRegisteredName() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Angie"));
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.name())
                    .isEqualTo("Angie");
        }

        @Test
        void newPlayerAccountHasZeroBalance() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"));
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.balance())
                    .isZero();
        }

        @Test
        void moneyDeposited10HasBalance10() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"),
                                                      new MoneyDeposited(10));
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.balance())
                    .isEqualTo(10);
        }

        @Test
        void moneyDepositedMultipleTimes() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Jane"),
                                                      new MoneyDeposited(53),
                                                      new MoneyDeposited(25));
            PlayerAccount playerAccount = new PlayerAccount(events);

            assertThat(playerAccount.balance())
                    .isEqualTo(53 + 25);
        }
    }

}
