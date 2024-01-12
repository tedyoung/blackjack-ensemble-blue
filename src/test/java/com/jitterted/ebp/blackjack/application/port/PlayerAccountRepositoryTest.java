package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountRepositoryTest {

    @Test
    void loadPlayerAccountByTheIdAssignedDuringSave() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        PlayerAccount savedAccount = playerAccountRepository.save(PlayerAccount.register("IrrelevantName"));

        PlayerId playerId = savedAccount.getId();
        Optional<PlayerAccount> foundAccount = playerAccountRepository.find(playerId);

        assertThat(foundAccount)
                .contains(savedAccount);
    }

    @Test
    void assignsUniqueIdOnSaveOfNewAccount() {
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(79);

        PlayerAccount firstAccount = playerAccountRepository.save(PlayerAccount.register("First"));
        PlayerAccount secondAccount = playerAccountRepository.save(PlayerAccount.register("Second"));

        assertThat(secondAccount.getId())
                .isNotEqualTo(firstAccount.getId());
    }

    @Test
    void loadReturnsEmptyOptionalWhenIdDoesNotExist() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();

        Optional<PlayerAccount> playerAccount = playerAccountRepository.find(PlayerId.of(29));

        assertThat(playerAccount)
                .isEmpty();
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

        PlayerAccount loadedAccount = playerAccountRepository.find(PlayerId.of(78)).orElseThrow();

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

        PlayerAccount alice = playerAccountRepository.find(PlayerId.of(78)).orElseThrow();
        PlayerAccount bob = playerAccountRepository.find(PlayerId.of(92)).orElseThrow();

        assertThat(alice.name())
                .isEqualTo("Alice");
        assertThat(alice.getId())
                .isEqualTo(PlayerId.of(78));
        assertThat(bob.name())
                .isEqualTo("Bob");
        assertThat(bob.getId())
                .isEqualTo(PlayerId.of(92));
    }

    private void createAndSavePlayerAccount(String name, int id, PlayerAccountRepository playerAccountRepository) {
        PlayerAccount firstPlayerAccount = PlayerAccount.register(name);
        firstPlayerAccount.setId(PlayerId.of(id));
        playerAccountRepository.save(firstPlayerAccount);
    }
}
