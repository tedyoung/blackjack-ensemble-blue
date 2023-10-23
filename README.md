> Note this repo has been renamed as it is now used by a long-running Ensemble group.
> If you'd like to join the group, let Ted know. 

# Make Your Blackjack Code More Testable

This is the starter code base for the "Make Code More Testable" class.

# Requirements

You **must have** the following already set up and running on your machine:

* Java 21 or later
* IntelliJ IDEA 2023.2.x or later

# Setting Up Project

Before opening up the project using your development environment, make sure that you can build the project from the command-line by running:

```sh
mvn verify
``` 

If you get errors about dependencies being unavailable, that means your machine isn't set up correctly to get the dependencies from your artifact repository. Please work with your tech support or your colleagues to resolve this issue as it completely depends on your machine's VPN and security set up.

Once the project compiles from the command-line, open it in your development environment and ensure that you can run all of the unit tests -- if everything is set up properly, they will all pass.

## Running and Playing the Game

To run the game, it works best if you run it in a real terminal window and not inside the IDE.
To do this, use Maven to build and package the JAR and then you can run it:

```sh
mvn package
```

This will download dependencies, compile the code, run the tests, and then package it into a JAR file that it will put in the `target` directory. To run it, do the following:

```sh
java -jar target/blackjack-1.0.0.jar
```

## Rules of Blackjack

Players play against the Dealer, not each other. The goal of the game is to get a hand with a score as close to 21 without going over, as well as beating the score of the Dealer's hand. The score of the hand is the sum of the point values of its cards. If a Player's hand score is over 21, the Player loses ("busts").

Blackjack uses a standard deck of 52 playing cards. Each card has a rank: Ace (A), 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack (J), Queen (Q), or King (K). The card's rank determines its scoring value: numbered cards 2-10 are worth their number; face cards (Jack, Queen, King) are worth 10 points; Aces are worth 1 point or 11 points, whichever would bring the hand value closer to 21 without exceeding it.

Each game proceeds as follows, starting with the dealing of cards:

* The dealer shuffles the deck and deals as follows:
  
  * Cards are dealt one at a time to each player in a round-robin fashion (technically clock-wise, starting on the dealer's left)
  * A card is dealt to the dealer face down, showing only the back of the card (so players can't see the rank of the card)

  At this point all of the players and the dealer have one card.
 
  * Cards are dealt to the players again
  * A card is dealt to the dealer, this time face up (the rank is visible)
    
  Now everyone has two cards.

  Any player who has Blackjack (an Ace with a ten-valued card) immediately wins (unless the dealer also has Blackjack) and does not take a turn the rest of the game.

* Play begins with the first player:
  
    * The dealer asks the first player whether they want to "hit" (get another card from the deck) or "stand" (not get any more cards).
    * If they choose to stand, their turn is over and the game moves on to the next player, if any.
    * If the player hits, the dealer deals them a card from the deck, face up, and their hand score changes based on the additional card.
    * If this causes their score to go over 21, they have "busted" and immediately lose the round and play continues with the next player.
    * If they haven't busted, they continue to have the option to hit or stand again.

Once all players have completed their turn, it's the dealer's turn to play, unless all players busted, in which case the game simply ends. The dealer turns their face-down card over to be visible. They then hit until their score is 17 or higher at which point they must stand. The dealer cannot decide to hit or stand, if their hand's score is 16 or lower, they must hit, and at 17 or higher, they must stand.

If the dealer busts, any players who did not bust earlier automatically win the round. Otherwise, each player wins if they have a strictly higher score than the dealer, or loses if they have a strictly lower score, and "pushes" (ties) if they have the same (tie) score.
