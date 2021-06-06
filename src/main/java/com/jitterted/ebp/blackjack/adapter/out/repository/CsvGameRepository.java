package com.jitterted.ebp.blackjack.adapter.out.repository;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.port.GameRepository;

import java.io.File;
import java.io.IOException;

public class CsvGameRepository implements GameRepository {

    private final File file;

    public CsvGameRepository(File file) throws IOException {
        this.file = file;
        file.createNewFile();
    }

    @Override
    public void saveOutcome(Game game) {

    }
}
