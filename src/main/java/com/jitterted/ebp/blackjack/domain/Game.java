package com.jitterted.ebp.blackjack.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private final DealerHand dealerHand = new DealerHand();
    private final List<PlayerInGame> players;
    private final Iterator<PlayerInGame> playerIterator;
    private PlayerInGame currentPlayer;
    private final Shoe shoe;

    private GameState gameState = GameState.AWAITING_BETS;

    @Deprecated
    public Game(Shoe shoe, PlayerCount playerCount) {
        this.shoe = shoe;
        this.players = create(playerCount);
        playerIterator = this.players.listIterator();
        currentPlayer = playerIterator.next();
    }

    public Game(Shoe shoe, List<PlayerId> playerIds) {
        PlayerCount playerCount = PlayerCount.of(playerIds.size());
        this.shoe = shoe;
        players = new ArrayList<>();
        for (int i = 0; i < playerCount.playerCount(); i++) {
            players.add(new PlayerInGame(playerIds.get(i).id()));
        }
        playerIterator = this.players.listIterator();
        currentPlayer = playerIterator.next();
    }

    private static List<PlayerInGame> create(PlayerCount numberOfPlayers) {
        List<PlayerInGame> players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers.playerCount(); i++) {
            players.add(new PlayerInGame(i));
        }
        return players;
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
        players.forEach(player -> {
            player.initialDrawFrom(shoe);
        });
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
        return players.stream()
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
        return players.stream().allMatch(PlayerInGame::isDone);
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
        return players.stream()
                .anyMatch(player -> player.reasonDone().equals(PlayerReasonDone.PLAYER_STANDS));
    }

    private void tellAllPlayersAreDoneDealerBlackjack() {
        players.forEach(PlayerInGame::doneDealerDealtBlackjack);
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
        return players.size();
    }

    public List<PlayerDoneEvent> events() {
        return players.stream()
                .flatMap(player -> player.playerDoneEvent().stream())
                .toList();
    }

    public int currentPlayerId() {
        return currentPlayer.id();
    }

    public void placeBets(List<Bet> placedBets) {
        requireCardsNotDealt();
        requireNoBetsPlaced();
        requireBetsMatchPlayerCount(placedBets);

        players.forEach(player -> player.placeBet(placedBets.get(player.id())));
        gameState = GameState.BETS_PLACED;
    }

    public List<PlayerBet> currentBets() {
        if (gameState == GameState.AWAITING_BETS) {
            return Collections.emptyList();
        }
        return players.stream()
                      .map(playerInGame -> new PlayerBet(new PlayerId(playerInGame.id()),
                                                         playerInGame.bet()))
                      .toList();
    }

    private void requireNoBetsPlaced() {
        if (gameState == GameState.BETS_PLACED) {
            throw new BetsAlreadyPlaced();
        }
    }

    private void requireBetsMatchPlayerCount(List<Bet> bets) {
        if (bets.size() != playerCount()) {
            throw new BetsNotMatchingPlayerCount();
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

    private enum GameState {
        AWAITING_BETS,
        BETS_PLACED,
        CARDS_DEALT,
        GAME_OVER
    }
}
