package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountRepositoryTest {

    @Test
    void loadPlayerAccount() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();

        PlayerAccount playerAccount = playerAccountRepository.load(PlayerId.of(29));

        assertThat(playerAccount.getId())
                .isEqualTo(PlayerId.of(29));
    }

    @Test
    void idIsGeneratedWhenSavingAccount() throws Exception {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();

        PlayerAccount savedAccount = playerAccountRepository.save(PlayerAccount.register("IrrelevantName"));

        assertThat(savedAccount.getId())
                .isNotNull();
    }
}
