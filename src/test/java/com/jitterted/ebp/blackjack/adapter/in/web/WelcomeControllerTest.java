package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;

class WelcomeControllerTest {
    @Test
    void homepagePopulatesPlayerSelectionForm() throws Exception {
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(35);
        playerAccountRepository.save(PlayerAccount.register("Jack Black"));

        WelcomeController welcomeController = new WelcomeController(playerAccountRepository);

        Model model = new ConcurrentModel();
        String templateName = welcomeController.home(model);

        assertThat(templateName)
                .isEqualTo("welcome");

        PlayerSelectionForm playerSelectionForm = (PlayerSelectionForm) model.getAttribute("playerSelectionForm");
        assertThat(playerSelectionForm)
                .isNotNull();
        assertThat(playerSelectionForm.getPlayers())
                .containsExactly(new PlayerAccountView(35, "Jack Black"));
    }
}