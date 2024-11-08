package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountFinder;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerDoneEvent;
import com.jitterted.ebp.blackjack.domain.PlayerId;
import com.jitterted.ebp.blackjack.domain.PlayerReasonDone;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class GameInProgressView {

    private EnumMap<PlayerReasonDone, String> playerReasonDoneMap;
    private int playerId;
    private List<String> dealerCards;
    private List<String> playerCards;
    private List<String> playerEvents;
    private String currentPlayerName;

    public GameInProgressView() {
        playerReasonDoneMap = new EnumMap<>(PlayerReasonDone.class);
        playerReasonDoneMap.put(PlayerReasonDone.PLAYER_HAS_BLACKJACK, "Player has blackjack");
        playerReasonDoneMap.put(PlayerReasonDone.PLAYER_BUSTED, "Player busted");
        playerReasonDoneMap.put(PlayerReasonDone.PLAYER_STANDS, "Player stands");
        playerReasonDoneMap.put(PlayerReasonDone.DEALER_DEALT_BLACKJACK, "Dealer dealt blackjack");
    }

    public static GameInProgressView of(Game game, PlayerAccountFinder playerAccountFinder) {
        GameInProgressView view = new GameInProgressView();

        view.dealerCards = CardMapper.dealerInProgressCardsAsString(game.dealerHand().cards());

        view.playerCards = CardMapper.cardsAsString(game.currentPlayerCards());
        view.playerId = game.currentPlayerId().id();
        view.currentPlayerName = findPlayerName(playerAccountFinder, game.currentPlayerId());
        view.playerEvents = game.events().stream()
                                .map(event -> view.reasonDoneForPlayerAsString(event, playerAccountFinder))
                                .collect(Collectors.toList());
        return view;
    }

    private static String findPlayerName(PlayerAccountFinder playerAccountFinder, PlayerId playerId) {
        return playerAccountFinder.find(playerId)
                                  .orElseThrow(() -> new IllegalArgumentException("Player not found in PlayerAccountFinder for: " +
                                                                                          playerId))
                                  .name();
    }

    public List<String> getDealerCards() {
        return dealerCards;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    public int getPlayerId() {
        return playerId;
    }

    public List<String> getPlayerEvents() {
        return playerEvents;
    }

    private String reasonDoneForPlayerAsString(PlayerDoneEvent event, PlayerAccountFinder playerAccountFinder) {
        String playerName = findPlayerName(playerAccountFinder, event.playerId());
        return playerName + ": " + playerReasonDoneMap.get(event.reasonDone());
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }
}
