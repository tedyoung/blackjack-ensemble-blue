package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.application.port.PlayerAccountFinder;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.PlayerDoneEvent;
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
    private String playerName;

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
        view.playerName = playerAccountFinder.find(game.currentPlayerId())
                                             .orElseThrow(() -> new IllegalArgumentException("Player not found in PlayerAccountFinder for: " + game.currentPlayerId()))
                                             .name();
        view.playerEvents = game.events().stream()
                                .map(view::reasonDoneForPlayerAsString)
                                .collect(Collectors.toList());
        return view;
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

    private String reasonDoneForPlayerAsString(PlayerDoneEvent event) {
        return event.playerId().id() + ": " + playerReasonDoneMap.get(event.reasonDone());
    }

    public String getCurrentPlayerName() {
        return playerName;
    }
}
