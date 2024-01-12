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

    @Test
    void idIsGeneratedWhenSavingNewAccount() {
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(54);

        PlayerAccount savedAccount = playerAccountRepository.save(PlayerAccount.register("IrrelevantName"));

        assertThat(savedAccount.getId())
                .isEqualTo(PlayerId.of(54));
    }

    @Test
    void existingIdIsNotOverwritten() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        PlayerAccount playerAccount = PlayerAccount.register("IrrelevantName");
        playerAccount.setId(PlayerId.of(78));

        PlayerAccount savedAccount = playerAccountRepository.save(playerAccount);

        assertThat(savedAccount.getId())
                .isEqualTo(PlayerId.of(78));
    }

    @Test
    void savedPlayerAccountCanBeLoaded() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        createAndSavePlayerAccount("Jane", 78, playerAccountRepository);

        PlayerAccount loadedAccount = playerAccountRepository.load(PlayerId.of(78));

        assertThat(loadedAccount.name())
                .isEqualTo("Jane");
        assertThat(loadedAccount.getId())
                .isEqualTo(PlayerId.of(78));
    }

    @Test
    void savedMultiplePlayerAccountsCanBeLoaded() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        createAndSavePlayerAccount("Alice", 78, playerAccountRepository);
        createAndSavePlayerAccount("Bob", 92, playerAccountRepository);

        PlayerAccount loadedAccount = playerAccountRepository.load(PlayerId.of(78));

        assertThat(loadedAccount.name())
                .isEqualTo("Alice");
        assertThat(loadedAccount.getId())
                .isEqualTo(PlayerId.of(78));
    }

    private void createAndSavePlayerAccount(String name, int id, PlayerAccountRepository playerAccountRepository) {
        PlayerAccount firstPlayerAccount = PlayerAccount.register(name);
        firstPlayerAccount.setId(PlayerId.of(id));
        playerAccountRepository.save(firstPlayerAccount);
    }
}
