package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.port.GameRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("integration")
class CsvGameRepositoryTest {
    @TempDir
    File tempDir;

    // Z O M B I E S

    @Test
    void givenEmptyRepositoryWhenInstantiatedCreatesNewCsvFile() throws Exception {
        File file = new File(tempDir, "outcome.csv");

        new CsvGameRepository(file);

        assertThat(file)
                .exists();
    }

    @Test
    void givenEmptyRepositoryWhenSaveOutcomeContainsOneLine() throws Exception{
        File file = new File(tempDir, "outcome.csv");
        GameRepository repository = new CsvGameRepository(file);

        fail("start here: read method name");

    }

    //    @Test
//    public void savesGameOutcomeIntoCsvFile() {
//        File file = new File(tempDir, "outcome.csv");
//        GameRepository repository = new CsvGameRepository(file);
//        Deck playerBeatsDealerDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
//                                                  Rank.TEN, Rank.JACK);
//        Game game = new Game(playerBeatsDealerDeck);
//        game.initialDeal();
//
//        game.playerStands();
//
//        repository.saveOutcome(game);
//
//        File file = new File(tempDir, "outcome.csv");
//        assertThat(file)
//            .exists()
//            .canRead();
//    }
}