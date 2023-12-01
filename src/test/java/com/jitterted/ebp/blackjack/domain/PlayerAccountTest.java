package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountTest {

    @Test
    void newPlayerAccountHasZeroBalance() {
        PlayerAccount playerAccount = new PlayerAccount(PlayerId.irrelevantPlayerId());

        assertThat(playerAccount.balance())
                .isZero();
    }

    @Test
    void deposit10HasBalance10() {
        MoneyDeposited event = new MoneyDeposited(10);
        PlayerAccount playerAccount = new PlayerAccount(PlayerId.irrelevantPlayerId(),
                                                        List.of(event));

        assertThat(playerAccount.balance())
                .isEqualTo(10);
    }
}
