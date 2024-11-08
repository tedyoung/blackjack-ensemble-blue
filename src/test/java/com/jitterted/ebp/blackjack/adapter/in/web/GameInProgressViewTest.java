package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountRepository;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameBuilder;
import com.jitterted.ebp.blackjack.domain.PlayerAccount;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.StubDeckBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameInProgressViewTest {

    @Test
    void twoPlayerGameHasNoEventsOnInitialDeal() {
        Deck deck = StubDeckBuilder.playerCountOf(2)
                                   .addPlayerNotDealtBlackjack()
                                   .addPlayerNotDealtBlackjack()
                                   .buildWithDealerDoesNotDrawCards();
        PlayerId firstPlayer = PlayerId.of(157);
        PlayerId secondPlayer = PlayerId.of(179);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck, firstPlayer, secondPlayer);
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(firstPlayer.id());
        playerAccountRepository.save(PlayerAccount.register("Mike"));

        GameInProgressView gameInProgressView = GameInProgressView.of(game, playerAccountRepository);

        assertThat(gameInProgressView.getPlayerEvents())
                .isEmpty();
    }

    @Test
    void twoPlayerGameHasEventAfterFirstPlayerStands() {
        StubDeck deck = StubDeckBuilder.playerCountOf(2)
                                       .addPlayerNotDealtBlackjack()
                                       .addPlayerNotDealtBlackjack()
                                       .buildWithDealerRanks(Rank.KING, Rank.FIVE);
        PlayerId firstPlayer = PlayerId.of(157);
        PlayerId secondPlayer = PlayerId.of(158);
        Game game = GameBuilder.createTwoPlayerGamePlaceBetsInitialDeal(deck, firstPlayer, secondPlayer);
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(firstPlayer.id());
        playerAccountRepository.save(PlayerAccount.register("Mike"));
        playerAccountRepository.save(PlayerAccount.register("Anna"));
        game.playerStands();

        GameInProgressView gameInProgressView = GameInProgressView.of(game, playerAccountRepository);

        assertThat(gameInProgressView.getPlayerEvents())
                .hasSize(1);
        assertThat(gameInProgressView.getPlayerEvents().get(0))
                .isEqualTo("Mike: Player stands");
    }

    @Test
    void threePlayerGameHasTwoEventsAfterFirstPlayerHasBlackjackAndSecondPlayerStands() {
        Deck deck = new StubDeck(Rank.JACK, Rank.TEN, Rank.KING, Rank.QUEEN,
                                 Rank.ACE, Rank.TWO, Rank.FIVE, Rank.EIGHT);
        Game game = GameBuilder.playerCountOf(3)
                               .deck(deck)
                               .addPlayer(PlayerId.of(23)) // blackjack
                               .addPlayer(PlayerId.of(24)) // stands
                               .addPlayer(PlayerId.of(25))
                               .placeDefaultBets()
                               .initialDeal()
                               .build();
        PlayerAccountRepository playerAccountRepository = PlayerAccountRepository.withNextId(23);
        playerAccountRepository.save(PlayerAccount.register("Mike"));
        playerAccountRepository.save(PlayerAccount.register("Anna"));
        playerAccountRepository.save(PlayerAccount.register("George"));

        game.playerStands();
        GameInProgressView gameInProgressView = GameInProgressView.of(game, playerAccountRepository);

        assertThat(gameInProgressView.getPlayerEvents())
                .hasSize(2);
        assertThat(gameInProgressView.getPlayerEvents().get(0))
                .isEqualTo("Mike: Player has blackjack");
        assertThat(gameInProgressView.getPlayerEvents().get(1))
                .isEqualTo("Anna: Player stands");
    }

    @Test
    void gameInitialDealThenDealerSecondCardFaceDown() {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .buildWithDealerRanks(Rank.SEVEN, Rank.QUEEN);
        Game game = GameBuilder.playerCountOf(1)
                               .deck(deck)
                               .addPlayer(PlayerId.of(37))
                               .placeDefaultBets()
                               .initialDeal()
                               .build();

        PlayerAccountRepository playerAccountRepository = new PlayerAccountRepository();
        playerAccountRepository.save(createPlayerAccountWithNameAndId("Mike", 37));

        GameInProgressView gameInProgressView = GameInProgressView.of(game, playerAccountRepository);

        assertThat(gameInProgressView.getDealerCards().get(0))
                .isEqualTo("7♥");
        assertThat(gameInProgressView.getDealerCards().get(1))
                .isEqualTo("XX");
    }

    private static PlayerAccount createPlayerAccountWithNameAndId(String name, int id) {
        PlayerAccount mike = PlayerAccount.register(name);
        mike.setPlayerId(PlayerId.of(id));
        return mike;
    }

    @Test
    void showsPlayerNameOfCurrentPlayer() {
        Deck deck = StubDeckBuilder.playerCountOf(1)
                                   .addPlayerWithRanks(Rank.SIX, Rank.TEN)
                                   .buildWithDealerRanks(Rank.SEVEN, Rank.QUEEN);
        PlayerId playerId = PlayerId.of(137);
        Game game = GameBuilder.playerCountOf(1)
                               .deck(deck)
                               .addPlayer(playerId)
                               .placeDefaultBets()
                               .initialDeal()
                               .build();
        PlayerAccountRepository repository = new PlayerAccountRepository();
        PlayerAccount george = PlayerAccount.register("George");
        george.setPlayerId(playerId);
        repository.save(george);

        GameInProgressView gameInProgressView = GameInProgressView.of(game, repository);

        assertThat(gameInProgressView.getCurrentPlayerName())
                .isEqualTo("George");
    }
}