package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlackjackController {

    private final GameService gameService;

    @Autowired
    public BlackjackController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start-game")
    public String startGame(int numberOfPlayers, @RequestParam(defaultValue = "") String customDeck) {
        createGame(numberOfPlayers, customDeck);
        Game game = gameService.currentGame();
        game.initialDeal();
        return redirectBasedOnGameState(game);
    }

    @GetMapping("/game")
    public String gameInProgressView(Model model) {
        Game game = gameService.currentGame();
        GameInProgressView gameInProgressView = GameInProgressView.of(game);
        model.addAttribute("gameInProgressView", gameInProgressView);
        return "game-in-progress";
    }

    @PostMapping("/hit")
    public String hitCommand() {
        Game game = gameService.currentGame();
        game.playerHits();
        return redirectBasedOnGameState(game);
    }

    @GetMapping("/done")
    public String viewDone(Model model) {
        Game game = gameService.currentGame();
        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game);
        model.addAttribute("gameOutcomeView", gameOutcomeView);
        return "done";
    }

    @PostMapping("/stand")
    public String standCommand() {
        Game game = gameService.currentGame();
        game.playerStands();
        return redirectBasedOnGameState(game);
    }

    private void createGame(int numberOfPlayers, String customDeck) {
        if (customDeck.isBlank()) {
            gameService.createGame(numberOfPlayers);
        } else {
            Deck deck = CustomDeckParser.createCustomDeck(customDeck);
            gameService.createGame(numberOfPlayers, deck);
        }
    }

    private String redirectBasedOnGameState(Game game) {
        if (game.isGameOver()) {
            return "redirect:/done";
        } else {
            return "redirect:/game";
        }
    }
}
