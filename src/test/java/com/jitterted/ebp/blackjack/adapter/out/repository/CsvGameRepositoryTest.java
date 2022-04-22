package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.port.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CsvGameRepositoryTest {
    @TempDir
    File tempDir;

    @Test
    void givenEmptyRepositoryWhenInstantiatedCreatesNewCsvFile() throws Exception {
        File file = new File(tempDir, "outcome.csv");

        new CsvGameRepository(file);

        assertThat(file)
            .exists();
    }

    @Test
    void givenEmptyRepositoryWhenSaveOutcomeContainsOneLine() throws Exception {
        File file = new File(tempDir, "outcome.csv");
        GameRepository repository = new CsvGameRepository(file);
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);
        game.initialDeal();

        repository.saveOutcome(game);

        assertThat(file)
            .hasContent("K♥/A♥,10♥/8♥,Player Wins Blackjack");
    }

    @Test
    public void givenRepositoryContainsOneGameResultWhenSaveOutcomeContainsTwoLines() throws Exception {
        File file = new File(tempDir, "outcome.csv");
        GameRepository repository = new CsvGameRepository(file);
        Game game = new Game(SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit(), 1);
        game.initialDeal();
        repository.saveOutcome(game);

        repository.saveOutcome(game);

        assertThat(Files.readAllLines(file.toPath()).size())
            .isEqualTo(2);
    }
}