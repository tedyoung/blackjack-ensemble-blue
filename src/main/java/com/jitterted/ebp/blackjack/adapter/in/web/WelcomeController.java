package com.jitterted.ebp.blackjack.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String home(Model model) {
        List<PlayerAccountView> players = List.of(new PlayerAccountView(42, "Jack Black"));
        model.addAttribute("playerSelectionForm", new PlayerSelectionForm(players));
        return "welcome";
    }

}
