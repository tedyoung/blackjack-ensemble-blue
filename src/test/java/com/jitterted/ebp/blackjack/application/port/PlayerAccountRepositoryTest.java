package com.jitterted.ebp.blackjack.application.port;

import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;

import java.util.List;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class PlayerAccountRepositoryTest {

    @Test
    void loadPlayerAccountByTheIdAssignedDuringSave() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        PlayerAccount savedAccount = playerAccountRepository.save(PlayerAccount.register("IrrelevantName"));

        PlayerId playerId = savedAccount.getPlayerId();
        PlayerAccount foundAccount = playerAccountRepository.find(playerId).orElseThrow();

        assertThat(foundAccount.getPlayerId())
                .isEqualTo(savedAccount.getPlayerId());
        assertThat(foundAccount)
                .isNotSameAs(savedAccount);
    }

    @Test
    void assignsUniqueIdOnSaveOfNewAccount() {
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(79);

        PlayerAccount firstAccount = playerAccountRepository.save(PlayerAccount.register("First"));
        PlayerAccount secondAccount = playerAccountRepository.save(PlayerAccount.register("Second"));

        assertThat(secondAccount.getPlayerId())
                .isNotEqualTo(firstAccount.getPlayerId());
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

        assertThat(savedAccount.getPlayerId())
                .isEqualTo(PlayerId.of(54));
    }

    @Test
    void existingIdIsNotOverwritten() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        PlayerAccount playerAccount = PlayerAccount.register("IrrelevantName");
        playerAccount.setPlayerId(PlayerId.of(78));

        PlayerAccount savedAccount = playerAccountRepository.save(playerAccount);

        assertThat(savedAccount.getPlayerId())
                .isEqualTo(PlayerId.of(78));
    }

    @Test
    void savedPlayerAccountCanBeLoaded() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        createAndSavePlayerAccount("Jane", 78, playerAccountRepository);

        PlayerAccount loadedAccount = playerAccountRepository.find(PlayerId.of(78)).orElseThrow();

        assertThat(loadedAccount.name())
                .isEqualTo("Jane");
        assertThat(loadedAccount.getPlayerId())
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
        assertThat(alice.getPlayerId())
                .isEqualTo(PlayerId.of(78));
        assertThat(bob.name())
                .isEqualTo("Bob");
        assertThat(bob.getPlayerId())
                .isEqualTo(PlayerId.of(92));
    }

    @Test
    void saveAppendsFreshEventsAndKeepsReconstitutedEvents() {
        PlayerAccountRepository repository = new PlayerAccountRepository();
        createAndSavePlayerAccount("Alice", 78, repository);
        PlayerAccount playerAccount = repository.find(PlayerId.of(78)).orElseThrow();
        playerAccount.deposit(2);
        repository.save(playerAccount);

        PlayerAccount reconsitutedPlayerAccount = repository.find(PlayerId.of(78)).orElseThrow();

        assertThat(reconsitutedPlayerAccount.name())
                .isEqualTo("Alice");
        assertThat(reconsitutedPlayerAccount.balance())
                .isEqualTo(2);
    }

    @Test
    void findAll() {
        PlayerAccountRepository repository = new PlayerAccountRepository();
        PlayerAccount alice = createAndSavePlayerAccount("Alice", 78, repository);
        PlayerAccount bob = createAndSavePlayerAccount("Bob", 81, repository);

        List<PlayerAccount> playerAccounts = repository.findAll();

        assertThat(playerAccounts)
                .hasSize(2)
                .containsExactlyInAnyOrder(alice, bob);
    }

    private PlayerAccount createAndSavePlayerAccount(String name, int playerId, PlayerAccountRepository playerAccountRepository) {
        PlayerAccount playerAccount = PlayerAccount.register(name);
        playerAccount.setPlayerId(PlayerId.of(playerId));
        playerAccountRepository.save(playerAccount);
        return playerAccount;
    }
}
