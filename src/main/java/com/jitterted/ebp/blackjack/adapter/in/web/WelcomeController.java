package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WelcomeController {

    private final PlayerAccountRepository playerAccountRepository;

    public WelcomeController(PlayerAccountRepository playerAccountRepository) {
        this.playerAccountRepository = playerAccountRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<PlayerAccountView> players = playerAccountRepository.findAll()
                                                              .stream()
                                                              .map(playerAccount -> new PlayerAccountView(playerAccount.getPlayerId().id(), playerAccount.name())).toList();

        model.addAttribute("playerSelectionForm", new PlayerSelectionForm(players));
        return "welcome";
    }

}
