package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlackjackController {

    private final GameService gameService;

    @Autowired
    public BlackjackController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start-game")
    public String startGame(int numberOfPlayers) {
        gameService.createGame(numberOfPlayers);
        gameService.currentGame().initialDeal();
        return "redirect:/game";
    }

    @GetMapping("/game")
    public String gameView(Model model) {
        Game game = gameService.currentGame();
        GameInProgressView gameInProgressView = GameInProgressView.of(game);
        model.addAttribute("gameInProgressView", gameInProgressView);
        model.addAttribute("command", new Command(game.currentPlayerId()));
        return "game-in-progress";
    }

    @PostMapping("/hit")
    public String hitCommand() {
        Game game = gameService.currentGame();
        game.playerHits();
        if (game.isGameOver()) {
            return "redirect:/done";
        } else {
            return "redirect:/game";
        }
    }

    @PostMapping("/stand")
    public String standCommand(Command command) throws InterruptedException {
        Thread.sleep(2000);
        if (command.getPlayerId() != gameService.currentGame().currentPlayerId()) {
            System.err.printf("Received command for player %d, but current player is %d", command.getPlayerId(), gameService.currentGame().currentPlayerId());
        } else {
            gameService.currentGame().playerStands();
        }
        if (gameService.currentGame().isGameOver()) {
            return "redirect:/done";
        }
        return "redirect:/game";
    }

    @GetMapping("/done")
    public String viewDone(Model model) {
        Game game = gameService.currentGame();
        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game);
        model.addAttribute("gameOutcomeView", gameOutcomeView);
        return "done";
    }

    static class Command {
        private int playerId;

        public Command(int playerId) {
            this.playerId = playerId;
        }

        public int getPlayerId() {
            return playerId;
        }

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }
    }
}
