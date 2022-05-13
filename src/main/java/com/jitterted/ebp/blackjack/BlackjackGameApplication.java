package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.out.repository.CsvGameRepository;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.GameMonitor;
import com.jitterted.ebp.blackjack.domain.GameService;
import com.jitterted.ebp.blackjack.domain.port.GameRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class BlackjackGameApplication {

    @Value("${deck:random}")
    private String deckConfiguration;

    public static void main(String[] args) {
        SpringApplication.run(BlackjackGameApplication.class, args);
    }

    @Bean
    public GameService createGameService(GameRepository gameRepository, GameMonitor gameMonitor) {
        if (isUnknownDeck()) {
            throw new IllegalArgumentException("unknown deck configuration");
        }
        Deck deck = new Deck();

        return new GameService(gameMonitor, gameRepository, deck);
    }

    private boolean isUnknownDeck() {
        return this.deckConfiguration != null
                && !(this.deckConfiguration.equals("random")
                    || this.deckConfiguration.equals("allPlayersDealtBlackjack"));
    }

    @Bean
    public GameMonitor createGameMonitor() {
        return game -> {}; // disable the use of the HttpGameMonitor();
    }

    @Bean
    public GameRepository createGameRepository() throws IOException {
        return new CsvGameRepository(new File("GameOutcome.csv"));
    }
}
