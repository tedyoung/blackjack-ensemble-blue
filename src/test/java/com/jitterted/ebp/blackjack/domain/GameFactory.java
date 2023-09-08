package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;


public class GameFactory {
//        GameBuilder.playerCountOf(2) // static
//                .deck(deck) // builds default deck if not specified
//                   .playerIds(firstPlayerId, secondPlayerId)
//                   .bets(firstBet, secondBet)
//
//                   .addPlayer()
//                   .addPlayer(firstPlayerId)
//                .addPlayer(firstPlayerId, firstBet)
//
//                   .addPlayer(secondPlayerId, secondBet)
//                .placeBets()
//                   .build;

    public static Game createMultiPlayerGamePlaceBetsInitialDeal(int playerCount, Deck deck) {
        List<PlayerId> playerIds = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            playerIds.add(new PlayerId(i + 50));
        }

        return createMultiPlayerGamePlaceBetsInitialDeal(playerIds, deck);
    }

    public static Game createMultiPlayerGamePlaceBetsInitialDeal(List<PlayerId> playerIds, Deck deck) {
        Game game = new Game(new Shoe(List.of(deck)), playerIds);
        List<PlayerBet> bets = new ArrayList<>();
        for (int i = 0; i < playerIds.size(); i++) {
            bets.add(new PlayerBet(playerIds.get(i), Bet.of(11 * (i + 1))));
        }
        game.placePlayerBets(bets);
        game.initialDeal();
        return game;
    }

    public static List<PlayerBet> createPlayerBets(PlayerCount playerCount) {
        List<PlayerBet> playerBets = new ArrayList<>();
        for (int i = 0; i < playerCount.playerCount(); i++) {
            PlayerBet playerBet = new PlayerBet(
                    new PlayerId((i * 10) + 1),
                    Bet.of((i + 1) * 6));
            playerBets.add(playerBet);
        }
        return playerBets;
    }
}
