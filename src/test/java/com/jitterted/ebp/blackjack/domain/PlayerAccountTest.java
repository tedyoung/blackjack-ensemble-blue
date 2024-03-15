package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountTest {

    public static final String IRRELEVANT_NAME = "John";

    @Test
    void reconstituteRequiresPlayerId() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> PlayerAccount.reconstitute(null, List.of()))
                .withMessage("reconstitute must have playerId");
    }

    @Nested
    class CommandsGenerateEvents {

        @Test
        void registeringPlayerEmitsPlayerRegistered() {
            EventSourcedAggregate playerAccount = PlayerAccount.register("John");

            Stream<PlayerAccountEvent> events = playerAccount.freshEvents();

            assertThat(events)
                    .containsExactly(new PlayerRegistered("John"));
        }

        @Test
        void depositEmitsMoneyDeposited() {
            PlayerAccount playerAccount = PlayerAccount.register(IRRELEVANT_NAME);

            playerAccount.deposit(55);

            assertThat(playerAccount.freshEvents())
                    .containsExactly(new PlayerRegistered(IRRELEVANT_NAME),
                                     new MoneyDeposited(55));
        }

        @Test
        void betEmitsMoneyBet() {
            PlayerAccount playerAccount = PlayerAccount.register(IRRELEVANT_NAME);
            playerAccount.deposit(55);

            playerAccount.bet(12);

            assertThat(playerAccount.freshEvents())
                    .containsExactly(new PlayerRegistered(IRRELEVANT_NAME),
                                     new MoneyDeposited(55),
                                     new MoneyBet(12));
        }

        @Test
        void winEmitsPlayerWonGame() {
            PlayerAccount playerAccount = PlayerAccount.register(IRRELEVANT_NAME);

            playerAccount.win(37, PlayerOutcome.DEALER_BUSTED);

            assertThat(playerAccount.freshEvents())
                    .containsExactly(
                            new PlayerRegistered(IRRELEVANT_NAME),
                            new PlayerWonGame(37, PlayerOutcome.DEALER_BUSTED)
                    );
        }

        @Test
        void loseEmitsPlayerLostGame() {
            PlayerAccount playerAccount = PlayerAccount.register(IRRELEVANT_NAME);

            playerAccount.lose(PlayerOutcome.PLAYER_LOSES);

            assertThat(playerAccount.freshEvents())
                    .containsExactly(
                            new PlayerRegistered(IRRELEVANT_NAME),
                            new PlayerLostGame(PlayerOutcome.PLAYER_LOSES)
                    );
        }
    }

    @Nested
    class EventsProjectState {

        @Test
        void newPlayerAccountHasRegisteredName() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered("Angie"));
            PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

            assertThat(playerAccount.name())
                    .isEqualTo("Angie");
        }

        @Test
        void newPlayerAccountHasZeroBalance() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(IRRELEVANT_NAME));
            PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

            assertThat(playerAccount.balance())
                    .isZero();
        }

        @Test
        void moneyDeposited10HasBalance10() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(IRRELEVANT_NAME),
                                                      new MoneyDeposited(10));
            PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

            assertThat(playerAccount.balance())
                    .isEqualTo(10);
        }

        @Test
        void moneyDepositedMultipleTimes() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(IRRELEVANT_NAME),
                                                      new MoneyDeposited(53),
                                                      new MoneyDeposited(25));
            PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

            assertThat(playerAccount.balance())
                    .isEqualTo(53 + 25);
        }

        @Test
        void bettingDecreasesBalance() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(IRRELEVANT_NAME),
                                                      new MoneyDeposited(20),
                                                      new MoneyBet(10));
            PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

            assertThat(playerAccount.balance())
                    .isEqualTo(20 - 10);
        }

        @Test
        void playerWonGameIncreasesBalance() {
            List<PlayerAccountEvent> events = List.of(new PlayerRegistered(IRRELEVANT_NAME),
                                                      new PlayerWonGame(37, PlayerOutcome.DEALER_BUSTED));
            PlayerAccount playerAccount = PlayerAccount.reconstitute(PlayerId.irrelevantPlayerId(), events);

            assertThat(playerAccount.balance())
                    .isEqualTo(37);
        }
    }

}
