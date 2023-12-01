package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountTest {

    @Test
    void newPlayerAccountHasZeroBalance() {
        PlayerAccount playerAccount = new PlayerAccount(PlayerId.irrelevantPlayerId());

        assertThat(playerAccount.balance())
                .isZero();
    }

    @Test
    public void deposit10HasBalanceOf10() throws Exception {
        PlayerAccount playerAccount = new PlayerAccount(PlayerId.irrelevantPlayerId());

        playerAccount.deposit(10);

        assertThat(playerAccount.balance())
                .isEqualTo(10);
    }
}
