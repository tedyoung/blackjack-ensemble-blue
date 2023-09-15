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
