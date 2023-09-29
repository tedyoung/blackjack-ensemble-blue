package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.domain.Bet;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Shoe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BlackjackController {

    private final GameService gameService;

    @Autowired
    public BlackjackController(GameService gameService) {
        this.gameService = gameService;
    }

    // NewGameForm: List<String> playersPlaying
    // createGameWithPlayers(NewGameForm)
    // validate (ensure it's an Integer), translate List<String> -> List<PlayerId>
    // gameService.createGame(List<PlayerId>)

    @PostMapping("/create-game")
    public String createGame(Integer numberOfPlayers, // <-- replace with NewGameForm
                             @RequestParam(defaultValue = "") String customDeck) {
        createNewGame(numberOfPlayers, customDeck);

        return "redirect:/place-bets";
    }

    public String createGame(NewGameForm newGameForm, String customDeck) {
        return null;
    }

    @GetMapping("/place-bets")
    public String showBettingForm(Model model) {
        ArrayList<Integer> bets = new ArrayList<>();
        for (int i = 0; i < gameService.currentGame().playerCount(); i++) {
            bets.add(0);
        }
        model.addAttribute("bettingForm", new BettingForm(bets));
        return "place-bets";
    }

    @PostMapping("/place-bets")
    public String placeBets(BettingForm bettingForm) {
        List<Bet> bets = bettingForm.getBets()
                                    .stream()
                                    .map(Bet::of)
                                    .toList();
        gameService.placeBets(bets);
        gameService.initialDeal();
        return redirectBasedOnGameState();
    }

    @PostMapping("/hit")
    public String hitCommand() {
        gameService.playerHits();
        return redirectBasedOnGameState();
    }

    @PostMapping("/stand")
    public String standCommand() {
        gameService.playerStands();
        return redirectBasedOnGameState();
    }

    private String redirectBasedOnGameState() {
        if (gameService.currentGame().isGameOver()) {
            return "redirect:/done";
        } else {
            return "redirect:/game";
        }
    }

    @GetMapping("/game")
    public String gameInProgressView(Model model) {
        Game game = gameService.currentGame();
        GameInProgressView gameInProgressView = GameInProgressView.of(game);
        model.addAttribute("gameInProgressView", gameInProgressView);
        return "game-in-progress";
    }

    @GetMapping("/done")
    public String viewDone(Model model) {
        Game game = gameService.currentGame();
        GameOutcomeView gameOutcomeView = GameOutcomeView.of(game);
        model.addAttribute("gameOutcomeView", gameOutcomeView);
        return "done";
    }

    private void createNewGame(int numberOfPlayers, String customDeck) {
        // get List<String> from the NewGameForm, convert to List<PlayerId>
        List<PlayerId> playerIds = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            playerIds.add(new PlayerId(i));
        }

        if (customDeck.isBlank()) {
            gameService.createGame(playerIds);
        } else {
            Deck deck = CustomDeckParser.createCustomDeck(customDeck);
            Shoe shoe = new Shoe(List.of(deck));
            gameService.createGame(playerIds, shoe);
        }
    }
}
