package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameFactory;
import com.jitterted.ebp.blackjack.domain.SinglePlayerStubDeckFactory;
import com.jitterted.ebp.blackjack.domain.StubDeck;
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
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(deck);

        repository.saveOutcome(game);

        assertThat(file)
                .hasContent("K♥/A♥,10♥/8♥,Player Wins Blackjack");
    }

    @Test
    public void givenRepositoryContainsOneGameResultWhenSaveOutcomeContainsTwoLines() throws Exception {
        File file = new File(tempDir, "outcome.csv");
        GameRepository repository = new CsvGameRepository(file);
        StubDeck deck = SinglePlayerStubDeckFactory.createPlayerDealtBlackjackDeckAndDealerCanNotHit();
        Game game = GameFactory.createOnePlayerGamePlaceBetsInitialDeal(deck);
        repository.saveOutcome(game);

        repository.saveOutcome(game);

        assertThat(Files.readAllLines(file.toPath()).size())
                .isEqualTo(2);
    }

}