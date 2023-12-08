package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountTest {

    @Test
    void newPlayerAccountHasZeroBalance() {
        List<PlayerAccountEvent> events = List.of(new PlayerRegistered());
        PlayerAccount playerAccount = new PlayerAccount(events);

        assertThat(playerAccount.balance())
                .isZero();
    }

    @Test
    void deposit10HasBalance10() {
        PlayerAccountEvent event = new MoneyDeposited(10);
        PlayerAccount playerAccount = new PlayerAccount(List.of(event));

        assertThat(playerAccount.balance())
                .isEqualTo(10);
    }

    @Test
    void registeringPlayerEmitsPlayerRegisteredEvent() {
        PlayerAccount playerAccount = PlayerAccount.register();

        List<PlayerAccountEvent> events = playerAccount.events();

        assertThat(events)
                .containsExactly(new PlayerRegistered());
    }
}
