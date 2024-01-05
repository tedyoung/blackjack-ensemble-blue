package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountRepositoryTest {
    @Test
    void loadPlayerAccount() throws Exception {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        PlayerAccount load = playerAccountRepository.load(PlayerId.of(29));

        assertThat(load)
                .isNotNull();
    }
}
