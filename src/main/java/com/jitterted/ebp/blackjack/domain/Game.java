package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class Game {

    private final DealerHand dealerHand = new DealerHand();
    private final List<PlayerInGame> playersInGame;
    private final Iterator<PlayerInGame> playerIterator;
    private PlayerInGame currentPlayer;
    private final Shoe shoe;

    private GameState gameState = GameState.AWAITING_BETS;

    public Game(List<PlayerId> playerIds, Shoe shoe) {
        PlayerCount playerCount = PlayerCount.of(playerIds.size());
        this.shoe = shoe;
        playersInGame = new ArrayList<>();
        for (int i = 0; i < playerCount.playerCount(); i++) {
            playersInGame.add(new PlayerInGame(playerIds.get(i).id()));
        }
        playerIterator = this.playersInGame.listIterator();
        currentPlayer = playerIterator.next();
    }

    // void initialDeal(List<PlayerId> players)
    public void initialDeal() {
        if (gameState == GameState.AWAITING_BETS) {
            throw new BetsNotPlaced();
        }

        dealRoundOfCards();
        dealRoundOfCards();
        gameState = GameState.CARDS_DEALT;
        if (dealerHand.hasBlackjack()) {
            tellAllPlayersAreDoneDealerBlackjack();
        }
        skipToNextPlayerIfDoneAndMaybeTakeDealerTurnAndEndGame();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        playersInGame.forEach(player -> player.initialDrawFrom(shoe));
        dealerHand.drawFrom(shoe);
    }

    public Hand dealerHand() {
        return dealerHand;
    }

    public PlayerOutcome currentPlayerOutcome() {
        return currentPlayer.outcome(dealerHand);
    }

    public int currentPlayerHandValue() {
        return currentPlayer.handValue();
    }

    public List<Card> currentPlayerCards() {
        // not allowed to ask before initial deal
        // not allowed when game is done (Protocol Violation)
        return currentPlayer.cards();
    }

    public List<PlayerResult> playerResults() {
        return playersInGame.stream()
                            .map(player -> new PlayerResult(player,
                                                      player.outcome(dealerHand),
                                                      player.bet()))
                            .collect(Collectors.toList());
    }

    // handle a player-based state change
    private void skipToNextPlayerIfDoneAndMaybeTakeDealerTurnAndEndGame() {
        skipDonePlayers();


        if (isDealerTurn()) {
            dealerTurn();
        }
    }

    private boolean isDealerTurn() {
        return playersInGame.stream().allMatch(PlayerInGame::isDone);
    }

    private void skipDonePlayers() {
        while (currentPlayer.isDone() && playerIterator.hasNext()) {
            currentPlayer = playerIterator.next();
        }
    }

    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    private void dealerTurn() {
        dealerHand.flipTheHoleCardUp();

        if (playersHaveUnknownOutcome()) {// Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
            while (dealerHand.dealerMustDrawCard()) {
                dealerHand.drawFrom(shoe);
            }
        }

        gameState = GameState.GAME_OVER;
    }

    private boolean playersHaveUnknownOutcome() {
        return playersInGame.stream()
                            .anyMatch(player -> player.reasonDone().equals(PlayerReasonDone.PLAYER_STANDS));
    }

    private void tellAllPlayersAreDoneDealerBlackjack() {
        playersInGame.forEach(PlayerInGame::doneDealerDealtBlackjack);
    }

    public void playerHits() {
        requireGameNotOver();
        requireCardsDealt();
        currentPlayer.hit(shoe);
        skipToNextPlayerIfDoneAndMaybeTakeDealerTurnAndEndGame();
    }

    public void playerStands() {
        requireGameNotOver();
        requireCardsDealt();
        currentPlayer.stand();
        skipToNextPlayerIfDoneAndMaybeTakeDealerTurnAndEndGame();
    }


    public int playerCount() {
        return playersInGame.size();
    }

    public List<PlayerDoneEvent> events() {
        return playersInGame.stream()
                            .flatMap(player -> player.playerDoneEvent().stream())
                            .toList();
    }

    public int currentPlayerId() {
        return currentPlayer.id();
    }

    @Deprecated
    public void placeBets(List<Bet> placedBets) {
        List<PlayerBet> playerBets = new ArrayList<>();
        for (int i = 0; i < playersInGame.size(); i++) {
            playerBets.add(new PlayerBet(new PlayerId(playersInGame.get(i).id()), placedBets.get(i)));
        }
        placePlayerBets(playerBets);
    }


    public void placePlayerBets(List<PlayerBet> placedBets) {
        requireCardsNotDealt();
        requireNoBetsPlaced();
        requireBetsMatchPlayerCount(placedBets);

        Map<PlayerId, PlayerInGame> playerMap = mapPlayersToIds();
        placedBets.forEach(playerBet -> {
            requirePlayerInGame(playerBet, playerMap);
            PlayerInGame playerInGame = playerMap.get(playerBet.playerId());
            playerInGame.placeBet(playerBet.bet());
        });

        gameState = GameState.BETS_PLACED;
    }

    private Map<PlayerId, PlayerInGame> mapPlayersToIds() {
        return playersInGame.stream()
                            .collect(toMap(
                            player -> new PlayerId(player.id()),
                            Function.identity()
                      ));
    }

    private void requirePlayerInGame(PlayerBet playerBet, Map<PlayerId, PlayerInGame> playerMap) {
        if (!playerMap.containsKey(playerBet.playerId())) {
            throw new IllegalArgumentException(
                    String.format("Player ID %s was not found, player IDs in game: %s",
                                  playerBet.playerId().id(),
                                  playerMap.keySet().stream()
                                           .map(PlayerId::id)
                                           .sorted()
                                           .toList()
                    )
            );
        }
    }

    private void requireBetsMatchPlayerCount(List<PlayerBet> placedBets) {
        if (placedBets.size() != playerCount()) {
            throw new BetsNotMatchingPlayerCount();
        }
    }

    public List<PlayerBet> currentBets() {
        if (gameState == GameState.AWAITING_BETS) {
            return Collections.emptyList();
        }
        return playersInGame.stream()
                            .map(playerInGame -> new PlayerBet(new PlayerId(playerInGame.id()),
                                                         playerInGame.bet()))
                            .toList();
    }

    private void requireNoBetsPlaced() {
        if (gameState == GameState.BETS_PLACED) {
            throw new BetsAlreadyPlaced();
        }
    }

    private void requireCardsDealt() {
        if (!cardsDealt()) {
            throw new CardsNotDealt();
        }
    }

    private void requireCardsNotDealt() {
        if (cardsDealt()) {
            throw new CannotPlaceBetsAfterInitialDeal();
        }
    }

    private void requireGameNotOver() {
        if (gameState == GameState.GAME_OVER) {
            throw new GameAlreadyOver();
        }
    }

    private boolean cardsDealt() {
        return gameState == GameState.CARDS_DEALT;
    }

    public List<PlayerId> playerIds() {
        return playersInGame.stream()
                            .map(player -> PlayerId.of(player.id()))
                            .toList();
    }

    private enum GameState {
        AWAITING_BETS,
        BETS_PLACED,
        CARDS_DEALT,
        GAME_OVER
    }
}
