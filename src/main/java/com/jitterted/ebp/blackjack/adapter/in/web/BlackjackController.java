package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.GameService;
import com.jitterted.ebp.blackjack.application.port.PlayerAccountFinder;
import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
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

import java.util.List;

@Controller
public class BlackjackController {

    private final GameService gameService;
    private final PlayerAccountFinder playerAccountFinder;

    @Autowired
    public BlackjackController(GameService gameService) {
        this.gameService = gameService;
        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
//        playerAccountRepository.save(PlayerAccount.register());
        playerAccountFinder = playerAccountRepository;
    }

    @PostMapping("/create-game")
    public String createGame(PlayerSelectionForm playerSelectionForm,
                             @RequestParam(defaultValue = "") String customDeck) {
        createNewGame(customDeck, playerSelectionForm.getPlayerIds());
        return "redirect:/place-bets";
    }

    @GetMapping("/place-bets")
    public String showBettingForm(Model model) {
        // Do we need / are allowed to access PlayerAccountRepository here?
        BettingForm form = BettingForm.zeroBetsFor(
                playerAccountFinder,
                gameService.currentGame().playerIds());
        model.addAttribute("bettingForm", form);
        return "place-bets";
    }

    @PostMapping("/place-bets")
    public String placeBets(BettingForm bettingForm) {
        gameService.placePlayerBets(bettingForm.getPlayerBets());
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

    private void createNewGame(String customDeck, List<PlayerId> playerIds) {
        if (customDeck.isBlank()) {
            gameService.createGame(playerIds);
        } else {
            Deck deck = CustomDeckParser.createCustomDeck(customDeck);
            Shoe shoe = new Shoe(List.of(deck));
            gameService.createGame(playerIds, shoe);
        }
    }
}
