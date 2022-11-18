package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.domain.Game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class CsvGameRepository implements GameRepository {

    private final File file;

    public CsvGameRepository(File file) throws IOException {
        this.file = file;
        file.createNewFile();
    }

    @Override
    public void saveOutcome(Game game) {
        try {
            String gameResultStringWithNewLine = new GameResultDto(game).asString() + "\n";
            Files.writeString(file.toPath(),
                              gameResultStringWithNewLine,
                              StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
