package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class WalletTest {

    @Test
    void newWalletIsEmpty() throws Exception {
        Wallet wallet = new Wallet();

        assertThat(wallet.isEmpty())
                .isTrue();
    }

    @Test
    void newWalletAddMoneyIsNotEmpty() throws Exception {
        Wallet wallet = new Wallet();

        wallet.addMoney(10);

        assertThat(wallet.isEmpty())
                .isFalse();
    }

    @Test
    void newWalletHasZeroBalance() throws Exception {
        Wallet wallet = new Wallet();

        assertThat(wallet.balance())
                .isZero();
    }

    @Test
    void newWalletAdd15HasBalanceOf15() throws Exception {
        Wallet wallet = new Wallet();

        wallet.addMoney(15);

        assertThat(wallet.balance())
                .isEqualTo(15);
    }

    @Test
    void newWalletAdd17And18HasBalanceOf35() throws Exception {
        Wallet wallet = new Wallet();

        wallet.addMoney(17);
        wallet.addMoney(18);

        assertThat(wallet.balance())
                .isEqualTo(17 + 18);
    }

    @Test
    void addMoneyOfLessThanZeroThrowsException() throws Exception {
        Wallet wallet = new Wallet();

        assertThatThrownBy(() -> {
            wallet.addMoney(-1);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
