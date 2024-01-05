package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountRepositoryTest {
    @Test
    @Disabled
    void loadPlayerAccount() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();

        PlayerAccount playerAccount = playerAccountRepository.load(PlayerId.of(29));

        assertThat(playerAccount.getId())
                .isEqualTo(PlayerId.of(29));
    }
}
