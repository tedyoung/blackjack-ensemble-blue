package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.out.repository.CsvGameRepository;
import com.jitterted.ebp.blackjack.adapter.out.shuffler.RandomShuffler;
import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.GameMonitor;
import com.jitterted.ebp.blackjack.application.port.GameRepository;
import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class BlackjackGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackjackGameApplication.class, args);
    }

    @Bean
    public GameService createGameService(GameRepository gameRepository,
                                         GameMonitor gameMonitor) {

        return new GameService(gameMonitor,
                               gameRepository,
                               new RandomShuffler()
        );
    }

    @Bean
    public GameMonitor createGameMonitor() {
        return game -> {
        }; // disable the use of the HttpGameMonitor();
    }

    @Bean
    public GameRepository createGameRepository() throws IOException {
        return new CsvGameRepository(new File("GameOutcome.csv"));
    }

    @Bean
    public PlayerAccountRepository createPlayerAccountRepository() {
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        playerAccountRepository.save(PlayerAccount.register("Steve Albini"));
        playerAccountRepository.save(PlayerAccount.register("David Bowie"));
        return playerAccountRepository;
    }
}
