package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.out.gamemonitor.HttpGameMonitor;
import com.jitterted.ebp.blackjack.adapter.out.repository.CsvGameRepository;
import com.jitterted.ebp.blackjack.domain.GameService;
import com.jitterted.ebp.blackjack.domain.port.GameRepository;
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
    public GameService createGameService(GameRepository gameRepository) {
        return new GameService(new HttpGameMonitor(), gameRepository);
    }

    @Bean
    public GameRepository createGameRepository() throws IOException {
        return new CsvGameRepository(new File("GameOutcome.csv"));
    }
}
